package com.example.library.tools;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * TODOS : 
 * - pendant le transfert d'une image, il faut d'abord récupérer les dimensions de l'image. Il 
 *   faut voir si l'on peut le faire à la volée, sans besoin de transférer l'image physiquement
 *   sur le serveur. Si ce n'est pas le cas, il faut transférer l'image d'origine dans un 
 *   répertoire stockant des fichiers temporaires, récupérer les dimensions et, à la fin 
 *   de l'opération, supprimer le fichier. 
 * - prévoir un ScheduledTask qui se baladera dans le dossier avec les fichiers temporaires 
 *   pour y supprimer des fichiers qui sont trop anciens (par exemple > 2h) - mais seulement 
 *   dans le cas où l'on ne peut pas détecter les tailles d'une image à la volée
 * - tester la transparence des images transférées
 * - voir le problème d'un GIF qui devient une image en noir et blanc
 */
public class ImageTool {
    final Logger logger = LoggerFactory.getLogger(ImageTool.class);
    private static final String TYPE_WIDTH = "WIDTH";
    private static final String TYPE_HEIGHT = "HEIGHT";
    @Autowired
    private ServletContext servletContext;
    private Map<String, Integer> typesByExt = new HashMap<String, Integer>(){{
        // JPG = 5 : TYPE_3BYTE_BGR
        // PNG = 6 : TYPE_4BYTE_ABGR
        // GIF = 12 : TYPE_BYTE_BINARY
        put("jpg", BufferedImage.TYPE_3BYTE_BGR);
        put("png", BufferedImage.TYPE_4BYTE_ABGR);
        put("gif", BufferedImage.TYPE_BYTE_BINARY);
    }};
    private Map<String, Map<String, Map<String, Object>>> config;
    private String filesDir;
    private String temporaryDir;
    private File originalImageFile;
    private String basePath;
    private String fileSeparator = System.getProperty("file.separator");
    
    public void setConfig(Map<String, Map<String, Map<String, Object>>> config) {
        this.config = config;
    }
    
    public void setFilesDir(String filesDir) {
        this.filesDir = filesDir;
    }
    public void setTemporaryDir(String temporaryDir) {
        this.temporaryDir = temporaryDir;
    }
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    private Map<String, Map<String, Object>> getConfigForType(String type) {
        return config.get(type);
    }

    private void setBasePath() {
        basePath = servletContext.getRealPath("/") + fileSeparator+filesDir+fileSeparator;
    }
    
    private Map<String, Object> getConfigForTypeAndFileEntry(String type, String fileEntry) {
        if(getConfigForType(type) != null) return getConfigForType(type).get(fileEntry);
        return null;
    }

    /**
     * Uploads files to server. 
     * Returns a map contains the following entries : 
     * - result (boolean) : indicate if upload process was done successfully
     * - fileBasename (String) : basename (without any prefixes) of uploaded file
     * - files (List<String>) : list of uploaded files
     */
    public Map<String, Object> uploadFile(String type, CommonsMultipartFile image, String originalFileBasename) {
        setBasePath();
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Map<String, Object>> fileConfig = getConfigForType(type);
        boolean uploadResult = true;
        logger.info("=======> fileConfig " + fileConfig);
        if (fileConfig != null) {
            String ext = getFileExt(image.getOriginalFilename());
            String finalFilename = originalFileBasename+"."+ext;
            BufferedImage originalImage = uploadOriginalFile(image, ext, basePath, finalFilename);
            for (Map.Entry<String, Map<String, Object>> entry : fileConfig.entrySet()) {
                Map<String, Object> values = entry.getValue();
                logger.info("=======> entry " + entry);
                logger.info("==========> found basePath " + basePath);
                FileItem imageFileItem = image.getFileItem();
                try {
                    Integer[] dims = getImageDimensions((Integer) values.get("width"), (Integer) values.get("height"), originalImage.getWidth(), originalImage.getHeight(), (String) values.get("ratio"));
                    
                    String fileExt = (String) values.get("extension");
                    
                    // type of output file (JPG by default)
                    int imgType = originalImage.getType();
                    String imgExt = ext;
                    if (!fileExt.equals(ext) && typesByExt.containsKey(fileExt)) {
                        imgType = typesByExt.get(fileExt);
                        imgExt = fileExt;
                    }
                    logger.info("=> Filetype "+imgType);
                    
                    // Integer[] types = new Integer[]{
					// BufferedImage.TYPE_3BYTE_BGR ,
					// BufferedImage.TYPE_4BYTE_ABGR ,
					// BufferedImage.TYPE_4BYTE_ABGR_PRE ,
					// BufferedImage.TYPE_BYTE_BINARY ,
					// BufferedImage.TYPE_BYTE_GRAY ,
					// BufferedImage.TYPE_BYTE_INDEXED ,
					// BufferedImage.TYPE_CUSTOM ,
					// BufferedImage.TYPE_INT_ARGB ,
					// BufferedImage.TYPE_INT_ARGB_PRE ,
					// BufferedImage.TYPE_INT_BGR ,
					// BufferedImage.TYPE_INT_RGB ,
					// BufferedImage.TYPE_USHORT_555_RGB ,
					// BufferedImage.TYPE_USHORT_565_RGB ,
					// BufferedImage.TYPE_USHORT_GRAY 
					// };
                    // for(Integer typ : types) {
                    BufferedImage thumb = new BufferedImage(dims[0], dims[1], imgType);
                    // BufferedImage thumb = new BufferedImage(dims[0], dims[1], typ);
                    Graphics2D graphics = thumb.createGraphics();
                    graphics.setComposite(AlphaComposite.Src);
                    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.drawImage(originalImage, 0, 0, dims[0], dims[1], null);
                    graphics.dispose();
                    
                    if (!ImageIO.write(thumb, imgExt, new File(basePath+fileSeparator+
                                 (String)values.get("directory")+fileSeparator + 
                                 ((String)values.get("prefix"))+originalFileBasename+"."+imgExt))) throw new IOException("An error on thumbnail uploading");
                   //}
                } catch (IOException ioe) {
                    removeUploadedFiles(type, originalFileBasename);
                    logger.error("An IOException occured", ioe);
                    uploadResult = false;
                } catch (Exception e) {
                    removeUploadedFiles(type, originalFileBasename);
                    logger.error("An error occured on writing FileItem", e);
                    uploadResult = false;
                }
            }
            originalImageFile.delete();
            result.put("fileBasename", finalFilename);
            result.put("uploadResult", uploadResult);
        }
        return result;
    }
    
    /**
     * Get file extensions. Returns empty String if extensions can't be found.
     */
    protected String getFileExt(String filename) {
        return (filename.lastIndexOf(".") == -1)?"":filename.substring(filename.lastIndexOf(".")+1,filename.length());
    }
    
    /**
     * Returns new images dimensions. Returned array represents width (the first element, key
     * 0) and height (the second element, key 1).
     */
    protected Integer[] getImageDimensions(int thumbWidth, int thumbHeight, int imgWidth, int imgHeight, String ratio) {
        Integer[] dimensions = new Integer[2];
        dimensions[0] = imgWidth;
        dimensions[1] = imgHeight;
        // get longer side if ratio is AUTO
        if (ratio.equals("AUTO")) {
            boolean allowResize = true;
            String type = TYPE_WIDTH;
            // first, get longer side
            if(imgWidth > imgHeight) {
                if (imgWidth < thumbWidth) allowResize = false;
            } else if (imgWidth < imgHeight) {
                if (imgHeight < thumbHeight) allowResize = false;
                else type = TYPE_HEIGHT;
            } else if (imgWidth < thumbWidth && imgHeight < thumbHeight) {
                allowResize = false;
            }
            if (allowResize) {
                dimensions = calculateDimensions(thumbWidth, thumbHeight, imgWidth, imgHeight, type);
                logger.info("Calculating dimensions " + dimensions);
                /* TODO after if(type.equals(TYPE_WIDTH) && dimensions[1] > thumbHeight)
                {
                    dimensions = calculateDimensions(dimensions[0], dimensions[1], thumbWidth, thumbHeight, TYPE_HEIGHT);
                }
                else if(type.equals(TYPE_HEIGHT) && dimensions[0] > thumbWidth)
                {
                    dimensions = calculateDimensions(dimensions[0], dimensions[1], thumbWidth, thumbHeight, TYPE_WIDTH);
                }*/
            }
        } else if (ratio.equals("STATIC")) {
            dimensions[0] = thumbWidth;
            dimensions[1] = thumbHeight;
        }
        return dimensions;
    }
    
    protected Integer[] calculateDimensions(int thumbWidth, int thumbHeight, int imgWidth, int imgHeight, String type) {
        logger.info("Calculating dimensions with original image size " + imgWidth +" and " + imgHeight + " for thumbnail sizes : " + thumbWidth + " and " + thumbHeight);
        Integer[] dimensions = new Integer[2];
        if (type.equals(TYPE_WIDTH)) {
            dimensions[0] = thumbWidth;
            double ratio = imgWidth/thumbWidth;
            logger.info("Ratio : "+ratio);
            dimensions[1] = Math.round(imgHeight/Math.round(ratio));
        } else {
            dimensions[1] = thumbHeight;
            double ratio = imgHeight/thumbHeight;
            logger.info("Ratio : "+ratio);
            dimensions[0] = Math.round(imgWidth/Math.round(ratio));
        }
        return dimensions;
    }

    protected BufferedImage uploadOriginalFile(CommonsMultipartFile image, String ext, String basePath, String finalFilename) {
        try {
            FileItem imageFileItem = image.getFileItem();
            originalImageFile = new File(basePath+fileSeparator+temporaryDir+fileSeparator+finalFilename);
            imageFileItem.write(originalImageFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(imageFileItem.getInputStream());
            return ImageIO.read(bufferedInputStream);
        } catch (Exception e) {
            logger.error("An exception occured on uploading original image", e);
        }
        return null;
    }
    
    protected void removeUploadedFiles(String type, String basename) {
        for (Map.Entry<String, Map<String, Object>> entry : getConfigForType(type).entrySet()) {
            Map<String, Object> values = entry.getValue();
            try {
                new File(basePath+fileSeparator+(String)values.get("directory")+fileSeparator + 
                        ((String)values.get("prefix"))+basename+"."+(String)values.get("extension")).delete();
            } catch (Exception e) {
                logger.error("An exception occured on deleting "+basename+" for config " + values, e);
            }
        }
    }
}