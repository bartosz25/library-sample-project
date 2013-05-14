
function log(text)
{
    $log = $('#log');
    //Add text to log
    $log.append(($log.val()?"\n":'')+text);
}

function send(text)
{
    Server.send( 'message', text );
}

// TODO : rajouter une fonction : si déconnecté, rajouter un bouton sur la page "se reconnecter"