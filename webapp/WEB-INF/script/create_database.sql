-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `id_ad` int(5) NOT NULL AUTO_INCREMENT,
  `login_ad` varchar(20) NOT NULL,
  `password_ad` varchar(255) NOT NULL,
  `email_ad` varchar(255) NOT NULL,
  `created_ad` datetime NOT NULL,
  `last_logged_ad` datetime NOT NULL,
  `role_ad` text NOT NULL,
  `is_admin_ad` int(1) NOT NULL COMMENT '0 - no, 1 - yes',
  PRIMARY KEY (`id_ad`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `admin`
--

INSERT INTO `admin` (`id_ad`, `login_ad`, `password_ad`, `email_ad`, `created_ad`, `last_logged_ad`, `role_ad`, `is_admin_ad`) VALUES
(1, 'admin', '617d01a6c699d839ebd3c54e2c006ae1d8a87842', 'bartkonieczny@yahoo.fr', '2012-12-26 18:12:51', '2013-01-17 12:48:41', 'SUBSCRIBER_ADD,SUBSCRIBER_EDIT', 1);

-- --------------------------------------------------------

--
-- Structure de la table `book`
--

CREATE TABLE IF NOT EXISTS `book` (
  `id_bo` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `added_bo` datetime NOT NULL,
  `updated_bo` datetime DEFAULT NULL,
  `alias_bo` varchar(200) NOT NULL,
  PRIMARY KEY (`id_bo`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=23 ;


-- --------------------------------------------------------

--
-- Structure de la table `booking`
--

CREATE TABLE IF NOT EXISTS `booking` (
  `book_copy_id_bc` int(11) unsigned NOT NULL,
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `date_boo` date NOT NULL,
  `added_boo` datetime NOT NULL,
  `state_boo` int(1) NOT NULL DEFAULT '1' COMMENT '0 - freezed (waiting for user''s reaction), 1 - active',
  PRIMARY KEY (`book_copy_id_bc`,`subscriber_id_su`),
  KEY `subscriber_id_su` (`subscriber_id_su`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Structure de la table `book_category`
--

CREATE TABLE IF NOT EXISTS `book_category` (
  `book_id_bo` int(11) unsigned NOT NULL,
  `category_id_ca` int(4) unsigned NOT NULL,
  PRIMARY KEY (`book_id_bo`,`category_id_ca`),
  KEY `category_id_ca` (`category_id_ca`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Structure de la table `book_copy`
--

CREATE TABLE IF NOT EXISTS `book_copy` (
  `id_bc` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `book_id_bo` int(11) unsigned NOT NULL,
  `code_bc` char(10) NOT NULL,
  `condition_bc` int(1) NOT NULL,
  `state_bc` int(1) NOT NULL COMMENT '0 - booked, 1 - available for loan',
  PRIMARY KEY (`id_bc`),
  KEY `book_id_bo` (`book_id_bo`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=42 ;


--
-- Structure de la table `book_lang`
--

CREATE TABLE IF NOT EXISTS `book_lang` (
  `book_id_bo` int(11) unsigned NOT NULL,
  `lang_id_la` int(3) unsigned NOT NULL,
  `type_bl` char(4) NOT NULL,
  `value_bl` text NOT NULL,
  PRIMARY KEY (`book_id_bo`,`lang_id_la`,`type_bl`),
  KEY `lang_id_la` (`lang_id_la`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Structure de la table `book_writer`
--

CREATE TABLE IF NOT EXISTS `book_writer` (
  `book_id_bo` int(11) unsigned NOT NULL,
  `writer_id_wr` int(11) unsigned NOT NULL,
  PRIMARY KEY (`book_id_bo`,`writer_id_wr`),
  KEY `writer_id_wr` (`writer_id_wr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `borrowing` (
  `id_bor` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `book_copy_id_bc` int(11) unsigned NOT NULL,
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `date_bor` date NOT NULL,
  `return_bor` date NOT NULL,
  `return_alert_bor` int(1) DEFAULT '-1',
  `penalized_bor` int(1) NOT NULL COMMENT '0 - no penalty attached, 1 - penalty attached to this borrowing',
  `last_action_bor` datetime DEFAULT NULL,
  PRIMARY KEY (`id_bor`),
  KEY `book_copy_id_bc` (`book_copy_id_bc`),
  KEY `subscriber_id_su` (`subscriber_id_su`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=16 ;


--
-- Structure de la table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `id_ca` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `alias_ca` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id_ca`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;

--
-- Contenu de la table `category`
--

INSERT INTO `category` (`id_ca`, `alias_ca`) VALUES
(1, 'Arts'),
(2, 'Biographie'),
(3, 'Business'),
(4, 'Children'),
(5, 'Classics'),
(6, 'Comics'),
(7, 'Education'),
(8, 'History'),
(9, 'Humor'),
(10, 'IT'),
(11, 'Literature'),
(12, 'Medical'),
(13, 'Poetry'),
(14, 'Political'),
(15, 'Science'),
(16, 'SF'),
(17, 'Travel');

-- --------------------------------------------------------

--
-- Structure de la table `category_lang`
--

CREATE TABLE IF NOT EXISTS `category_lang` (
  `category_id_ca` int(4) unsigned NOT NULL,
  `lang_id_la` int(3) unsigned NOT NULL,
  `name_cl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id_ca`,`lang_id_la`),
  KEY `lang_id_la` (`lang_id_la`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `category_lang`
--

INSERT INTO `category_lang` (`category_id_ca`, `lang_id_la`, `name_cl`) VALUES
(1, 1, 'Arts'),
(1, 2, 'Arts'),
(1, 3, 'Sztukq'),
(2, 1, 'Biographies'),
(2, 2, 'Biographies'),
(2, 3, 'Biografie'),
(3, 1, 'Business'),
(3, 2, 'Affaires'),
(3, 3, 'Business'),
(4, 1, 'Children'),
(4, 2, 'Enfants'),
(4, 3, 'Dziecięce'),
(5, 1, 'Classics'),
(5, 2, 'Classique'),
(5, 3, 'Klasyka'),
(6, 1, 'Comics'),
(6, 2, 'BD'),
(6, 3, 'Komiksy'),
(7, 1, 'Education'),
(7, 2, 'Manuels scolaires'),
(7, 3, 'Podręczniki szkolne'),
(8, 1, 'History'),
(8, 2, 'Historique'),
(8, 3, 'Historyczne'),
(9, 1, 'Humor'),
(9, 2, 'Humour'),
(9, 3, 'Humor'),
(10, 1, 'IT'),
(10, 2, 'IT'),
(10, 3, 'IT'),
(11, 1, 'Literature'),
(11, 2, 'Litérature'),
(11, 3, 'Literatura'),
(12, 1, 'Medical'),
(12, 2, 'Médical'),
(12, 3, 'Medyczne'),
(13, 1, 'Poetry'),
(13, 2, 'Poèsie'),
(13, 3, 'Poezja'),
(14, 1, 'Political'),
(14, 2, 'Politique'),
(14, 3, 'Polityczne'),
(15, 1, 'Science'),
(15, 2, 'Science'),
(15, 3, 'Naukowe'),
(16, 1, 'SF'),
(16, 2, 'SF'),
(16, 3, 'SF'),
(17, 1, 'Travel'),
(17, 2, 'Voyages'),
(17, 3, 'Podróże');

-- --------------------------------------------------------

--
-- Structure de la table `chat`
--

CREATE TABLE IF NOT EXISTS `chat` (
  `time_ch` bigint(13) unsigned NOT NULL,
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `admin_id_ad` int(5) NOT NULL,
  `text_ch` varchar(500) NOT NULL,
  `state_ch` int(1) NOT NULL,
  PRIMARY KEY (`time_ch`,`subscriber_id_su`,`admin_id_ad`),
  KEY `subscriber_id_su` (`subscriber_id_su`),
  KEY `admin_id_ad` (`admin_id_ad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Structure de la table `lang`
--

CREATE TABLE IF NOT EXISTS `lang` (
  `id_la` int(3) unsigned NOT NULL AUTO_INCREMENT,
  `iso_la` char(2) NOT NULL,
  `name_la` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id_la`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `lang`
--

INSERT INTO `lang` (`id_la`, `iso_la`, `name_la`) VALUES
(1, 'EN', 'English'),
(2, 'FR', 'French'),
(3, 'PL', 'Polish');

-- --------------------------------------------------------

--
-- Structure de la table `log`
--

CREATE TABLE IF NOT EXISTS `log` (
  `id_lo` int(3) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id_ad` int(5) NOT NULL,
  `date_lo` datetime NOT NULL,
  `action_lo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_lo`),
  KEY `admin_id_ad` (`admin_id_ad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Structure de la table `newsletter`
--

CREATE TABLE IF NOT EXISTS `newsletter` (
  `id_ne` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id_ad` int(5) NOT NULL,
  `created_ne` datetime NOT NULL,
  `state_ne` int(1) NOT NULL COMMENT '0 - not send, 1 - sending, 2 - send',
  `title_ne` varchar(255) NOT NULL,
  `text_ne` text NOT NULL,
  `send_ne` datetime NOT NULL,
  `preferencies_ne` text NOT NULL,
  PRIMARY KEY (`id_ne`),
  KEY `admin_id_ad` (`admin_id_ad`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Structure de la table `newsletter_preferency`
--

CREATE TABLE IF NOT EXISTS `newsletter_preferency` (
  `id_np` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `newsletter_preferency_category_id_npc` int(11) unsigned NOT NULL,
  `code_np` char(8) NOT NULL,
  `default_np` varchar(80) NOT NULL,
  PRIMARY KEY (`id_np`),
  KEY `newsletter_preferency_category_id_npc` (`newsletter_preferency_category_id_npc`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `newsletter_preferency`
--

INSERT INTO `newsletter_preferency` (`id_np`, `newsletter_preferency_category_id_npc`, `code_np`, `default_np`) VALUES
(1, 1, 'LESSTHAN', 'Less than 10'),
(2, 1, 'MORETHAN', 'More than 10'),
(3, 2, 'LESSTHAN', 'Less than 5'),
(4, 2, 'MORETHAN', 'More than 5');

-- --------------------------------------------------------

--
-- Structure de la table `newsletter_preferency_category`
--

CREATE TABLE IF NOT EXISTS `newsletter_preferency_category` (
  `id_npc` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `code_npc` char(8) NOT NULL,
  `alias_npc` varchar(40) NOT NULL,
  PRIMARY KEY (`id_npc`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Contenu de la table `newsletter_preferency_category`
--

INSERT INTO `newsletter_preferency_category` (`id_npc`, `code_npc`, `alias_npc`) VALUES
(1, 'FREQUENC', 'Frequency'),
(2, 'BOOKSPER', 'Books read per week');

-- --------------------------------------------------------

--
-- Structure de la table `newsletter_preferency_category_lang`
--

CREATE TABLE IF NOT EXISTS `newsletter_preferency_category_lang` (
  `newsletter_preferency_category_id_npc` int(11) unsigned NOT NULL,
  `lang_id_la` int(3) unsigned NOT NULL,
  `value_npcl` varchar(50) NOT NULL,
  PRIMARY KEY (`newsletter_preferency_category_id_npc`,`lang_id_la`),
  KEY `lang_id_la` (`lang_id_la`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `newsletter_preferency_category_lang`
--

INSERT INTO `newsletter_preferency_category_lang` (`newsletter_preferency_category_id_npc`, `lang_id_la`, `value_npcl`) VALUES
(1, 2, 'Fréquence d''emprunt'),
(2, 2, 'Nombre de livres lus par semaine');

-- --------------------------------------------------------

--
-- Structure de la table `newsletter_preferency_lang`
--

CREATE TABLE IF NOT EXISTS `newsletter_preferency_lang` (
  `newsletter_preferency_id_np` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `lang_id_la` int(3) unsigned NOT NULL,
  `label_npl` varchar(80) NOT NULL,
  PRIMARY KEY (`newsletter_preferency_id_np`,`lang_id_la`),
  KEY `lang_id_la` (`lang_id_la`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `newsletter_preferency_lang`
--

INSERT INTO `newsletter_preferency_lang` (`newsletter_preferency_id_np`, `lang_id_la`, `label_npl`) VALUES
(1, 2, 'Moins que 10'),
(2, 2, 'Plus que 10'),
(3, 2, 'Moins que 5'),
(4, 2, 'Plus que 5');

-- --------------------------------------------------------

--
-- Structure de la table `newsletter_send`
--

CREATE TABLE IF NOT EXISTS `newsletter_send` (
  `newsletter_id_ne` int(11) unsigned NOT NULL,
  `newsletter_subscriber_id_ns` int(11) unsigned NOT NULL,
  `date_nse` datetime NOT NULL,
  `state_nse` int(1) NOT NULL COMMENT '0 - send but not opened, 1 - send and opened, 2 - wairing to be send',
  PRIMARY KEY (`newsletter_id_ne`,`newsletter_subscriber_id_ns`),
  KEY `newsletter_subscriber_id_ns` (`newsletter_subscriber_id_ns`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
--
-- Structure de la table `newsletter_subscriber`
--

CREATE TABLE IF NOT EXISTS `newsletter_subscriber` (
  `id_ns` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `subscriber_id_su` int(11) unsigned DEFAULT NULL,
  `email_ns` varchar(255) NOT NULL,
  `password_ns` varchar(255) NOT NULL,
  `date_ns` datetime NOT NULL,
  `state_ns` int(1) NOT NULL COMMENT '0 - not confirmed, 1 - confirmed, 2 - deleted',
  PRIMARY KEY (`id_ns`),
  KEY `subscriber_id_su` (`subscriber_id_su`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Structure de la table `newsletter_subscriber_preferency`
--

CREATE TABLE IF NOT EXISTS `newsletter_subscriber_preferency` (
  `newsletter_subscriber_id_ns` int(11) unsigned NOT NULL,
  `newsletter_preferency_category_id_npc` int(11) unsigned NOT NULL,
  `preferency_nsp` int(11) unsigned DEFAULT NULL,
  `value_nsp` varchar(200) NOT NULL,
  PRIMARY KEY (`newsletter_subscriber_id_ns`,`newsletter_preferency_category_id_npc`),
  KEY `newsletter_preferency_category_id_npc` (`newsletter_preferency_category_id_npc`),
  KEY `preferency_nsp` (`preferency_nsp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
--
-- Structure de la table `page_content`
--

CREATE TABLE IF NOT EXISTS `page_content` (
  `id_pag` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `placement_id_pl` int(11) unsigned NOT NULL,
  `lang_id_la` int(3) unsigned NOT NULL,
  `title_pag` varchar(255) NOT NULL,
  `url_pag` varchar(255) NOT NULL,
  `content_pag` text NOT NULL,
  `h1_pag` varchar(100) NOT NULL,
  `h2_pag` varchar(100) NOT NULL,
  `meta_title_pag` varchar(155) NOT NULL,
  `meta_desc_pag` varchar(155) NOT NULL,
  `meta_keywords_pag` varchar(155) NOT NULL,
  `added_pag` datetime NOT NULL,
  `modified_pag` datetime DEFAULT NULL,
  PRIMARY KEY (`id_pag`),
  KEY `placement_id_pl` (`placement_id_pl`),
  KEY `lang_id_la` (`lang_id_la`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Contenu de la table `page_content`
--

INSERT INTO `page_content` (`id_pag`, `placement_id_pl`, `lang_id_la`, `title_pag`, `url_pag`, `content_pag`, `h1_pag`, `h2_pag`, `meta_title_pag`, `meta_desc_pag`, `meta_keywords_pag`, `added_pag`, `modified_pag`) VALUES
(1, 1, 1, 'About us', 'about-us', 'About us exemple', 'About us', 'Informations about our library', 'Library - informations about our books', 'Descript', 'key', '2012-01-12 00:00:00', NULL),
(2, 2, 1, 'Find a book', 'find-a-book', 'Find book', 'Find book', 'Find the book that you are looking for', 'Library - search engine', 'Descript', 'key', '2012-01-12 00:00:00', NULL),
(3, 3, 1, 'Contact', 'contact', 'Contact exemple', 'Contact', 'Get in touch', 'Library - informations about our books', 'Descript', 'key', '2012-01-12 00:00:00', NULL),
(4, 1, 2, 'Nous', 'nous', 'Nous', 'Informations sur notre société', 'Qui, où, quoi et comment', 'Librairie - informations sur nos livres', 'Descript', 'key', '2012-01-12 00:00:00', NULL),
(5, 2, 2, 'Trouver un livre', 'trouver-un-livre', 'Trouver un livre', 'Trouver un livre', 'Troues le livre que tu cherches', 'Librairie - moteur de recherche', 'Descript', 'key', '2012-01-12 00:00:00', NULL),
(6, 3, 2, 'Contact', 'contact', 'Contact', 'Contact', 'Une question à poser, c''est ici', 'Librairie - contactez-nous', 'Descript', 'key', '2012-01-12 00:00:00', NULL),
(7, 1, 3, 'Nasza biblioteka', 'nasza-biblioteka', 'Nasza biblioteka', 'Informacje o bibliotece', 'Kto, gdzie, co i jak ?', 'Biblioteka - informacje o książkach', 'Descript', 'key', '2012-01-12 00:00:00', NULL),
(8, 2, 3, 'Znajdź książkę', 'znajdz-ksiazke', 'Znajdź książkę', 'Znajdź książkę', 'Znajdź to, czego szukasz', 'Biblioteka - wyszukiwarka', 'Descript', 'key', '2012-01-12 00:00:00', NULL),
(9, 3, 3, 'Kontakt', 'kontakt', 'Kontakt', 'Kontakt', 'Jeśli chcesz nam zadać jakieś pytanie, to tutaj', 'Biblioteka - kontakt', 'Descript', 'key', '2012-01-12 00:00:00', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `payment`
--

CREATE TABLE IF NOT EXISTS `payment` (
  `id_pa` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `payment_method_id_pm` int(5) unsigned NOT NULL,
  `ref_pa` varchar(255) NOT NULL,
  `date_pa` datetime NOT NULL,
  `type_pa` int(1) NOT NULL,
  `amount_pa` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id_pa`),
  KEY `payment_method_id_pm` (`payment_method_id_pm`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;

-- 
--
-- Structure de la table `payment_method`
--

CREATE TABLE IF NOT EXISTS `payment_method` (
  `id_pm` int(3) unsigned NOT NULL AUTO_INCREMENT,
  `code_pm` char(6) NOT NULL,
  `name_pm` varchar(30) NOT NULL,
  `service_class_pm` varchar(50) NOT NULL,
  `service_name_pm` varchar(50) NOT NULL,
  `validity_pm` text NOT NULL,
  PRIMARY KEY (`id_pm`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Contenu de la table `payment_method`
--

INSERT INTO `payment_method` (`id_pm`, `code_pm`, `name_pm`, `service_class_pm`, `service_name_pm`, `validity_pm`) VALUES
(5, 'paypal', 'PayPal', 'library.service.payment.Paypal', 'paypalService', '#borrowed >= 0 and #spentMoney >= 0 and #activityPoints >= 2'),
(6, 'cheque', 'Cheque', 'library.service.payment.Cheque', 'chequeService', '#borrowed > 80 and #spentMoney > 1000 and #activityPoints == 5'),
(7, 'banwir', 'Bankwire', 'library.service.payment.Bankwire', 'bankwireService', '#borrowed > 80 and #spentMoney > 1000 and #activityPoints == 5');
 
--
-- Structure de la table `penalty`
--

CREATE TABLE IF NOT EXISTS `penalty` (
  `time_pe` bigint(13) unsigned NOT NULL,
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `payment_id_pa` int(11) unsigned DEFAULT NULL,
  `payment_method_id_pm` int(5) unsigned DEFAULT NULL,
  `amount_pe` decimal(6,2) NOT NULL,
  `state_pe` int(1) NOT NULL,
  `about_pe` text NOT NULL,
  PRIMARY KEY (`time_pe`,`subscriber_id_su`),
  KEY `payment_id_pa` (`payment_id_pa`),
  KEY `subscriber_id_su` (`subscriber_id_su`),
  KEY `payment_method_id_pm` (`payment_method_id_pm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Structure de la table `persistent_logins`
--

CREATE TABLE IF NOT EXISTS `persistent_logins` (
  `username` varchar(64) DEFAULT NULL,
  `series` varchar(64) NOT NULL DEFAULT '',
  `token` varchar(64) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Structure de la table `placement`
--

CREATE TABLE IF NOT EXISTS `placement` (
  `id_pl` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name_pl` varchar(50) NOT NULL,
  `place_pl` int(1) NOT NULL,
  `place_code_pl` varchar(20) NOT NULL,
  PRIMARY KEY (`id_pl`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `placement`
--

INSERT INTO `placement` (`id_pl`, `name_pl`, `place_pl`, `place_code_pl`) VALUES
(1, 'top', 0, ''),
(2, 'left', 0, ''),
(3, 'footer', 0, '');

-- --------------------------------------------------------

--
-- Structure de la table `preferency`
--

CREATE TABLE IF NOT EXISTS `preferency` (
  `id_pr` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `code_pr` char(4) NOT NULL,
  `default_label_pr` varchar(100) NOT NULL,
  `type_pr` int(1) NOT NULL COMMENT '0 - site, 1 - web service',
  PRIMARY KEY (`id_pr`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Contenu de la table `preferency`
--

INSERT INTO `preferency` (`id_pr`, `code_pr`, `default_label_pr`, `type_pr`) VALUES
(1, 'BORA', 'Borrowing alerts', 1),
(2, 'ABOU', 'About you', 1),
(3, 'FROM', 'Referer site URL', 1),
(4, 'SYNC', 'Synchronization frequency', 1),
(5, 'SYMA', 'Manually synchronization date', 1);

-- --------------------------------------------------------

--
-- Structure de la table `preferency_lang`
--

CREATE TABLE IF NOT EXISTS `preferency_lang` (
  `preferency_id_pr` int(11) unsigned NOT NULL,
  `lang_id_la` int(3) unsigned NOT NULL,
  `value_pl` varchar(100) NOT NULL,
  PRIMARY KEY (`preferency_id_pr`,`lang_id_la`),
  KEY `lang_id_la` (`lang_id_la`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `preferency_lang`
--


-- --------------------------------------------------------

--
-- Structure de la table `question`
--

CREATE TABLE IF NOT EXISTS `question` (
  `id_qu` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `lang_id_la` int(3) unsigned NOT NULL,
  `title_qu` varchar(150) NOT NULL,
  `content_qu` text NOT NULL,
  `date_qu` datetime NOT NULL,
  `state_qu` int(1) NOT NULL,
  PRIMARY KEY (`id_qu`),
  KEY `subscriber_id_su` (`subscriber_id_su`),
  KEY `lang_id_la` (`lang_id_la`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;
 
-- --------------------------------------------------------

--
-- Structure de la table `reply`
--

CREATE TABLE IF NOT EXISTS `reply` (
  `id_re` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id_ad` int(5) NOT NULL,
  `question_id_qu` int(11) unsigned NOT NULL,
  `content_re` text NOT NULL,
  `date_re` datetime NOT NULL,
  `state_re` int(1) NOT NULL,
  PRIMARY KEY (`id_re`),
  KEY `question_id_qu` (`question_id_qu`),
  KEY `admin_id_ad` (`admin_id_ad`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;
 
--
-- Structure de la table `subscriber`
--

CREATE TABLE IF NOT EXISTS `subscriber` (
  `id_su` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `login_su` varchar(20) NOT NULL,
  `password_su` varchar(255) NOT NULL,
  `avatar_su` text NOT NULL,
  `email_su` varchar(255) NOT NULL,
  `created_su` datetime NOT NULL,
  `confirmed_su` int(1) NOT NULL,
  `blacklisted_su` int(1) NOT NULL,
  `booking_nb_su` int(1) NOT NULL COMMENT 'number of books which can be booked',
  `revival_su` int(2) NOT NULL,
  PRIMARY KEY (`id_su`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=21 ;

--
-- Structure de la table `subscriber_preferency`
--

CREATE TABLE IF NOT EXISTS `subscriber_preferency` (
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `preferency_id_pr` int(11) unsigned NOT NULL,
  `value_sp` varchar(100) NOT NULL,
  PRIMARY KEY (`subscriber_id_su`,`preferency_id_pr`),
  KEY `preferency_id_pr` (`preferency_id_pr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `subscriber_preferency`
--


-- --------------------------------------------------------

--
-- Structure de la table `subscription`
--

CREATE TABLE IF NOT EXISTS `subscription` (
  `id_sub` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `payment_id_pa` int(11) unsigned DEFAULT NULL,
  `payment_method_id_pm` int(5) unsigned NOT NULL,
  `type_sub` int(1) unsigned NOT NULL,
  `amount_sub` decimal(6,2) unsigned NOT NULL,
  `start_sub` date NOT NULL,
  `end_sub` date NOT NULL,
  PRIMARY KEY (`id_sub`),
  KEY `subscriber_id_su` (`subscriber_id_su`),
  KEY `payment_id_pa` (`payment_id_pa`),
  KEY `payment_methods_id_pm` (`payment_method_id_pm`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Structure de la table `subscription_transfer`
--

CREATE TABLE IF NOT EXISTS `subscription_transfer` (
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `to_subscriber` int(11) unsigned NOT NULL,
  `subscription_id_sub` int(11) unsigned NOT NULL,
  `date_st` datetime NOT NULL,
  `state_st` int(1) NOT NULL COMMENT '0 - waiting for reaction, 1 - accepted, 2 - refused',
  PRIMARY KEY (`subscriber_id_su`,`to_subscriber`),
  KEY `to_subscriber` (`to_subscriber`),
  KEY `subscription_id_su` (`subscription_id_sub`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `suggestion`
--

CREATE TABLE IF NOT EXISTS `suggestion` (
  `id_sug` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `subscriber_id_su` int(11) unsigned NOT NULL,
  `title_sug` varchar(155) NOT NULL,
  `quantity_sug` int(4) NOT NULL,
  `state_sug` int(1) NOT NULL,
  `delivery_sug` datetime DEFAULT NULL,
  PRIMARY KEY (`id_sug`),
  KEY `subscriber_id_su` (`subscriber_id_su`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Structure de la table `writer`
--

CREATE TABLE IF NOT EXISTS `writer` (
  `id_wr` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `firstname_wr` varchar(20) NOT NULL,
  `familyname_wr` varchar(30) NOT NULL,
  `born_wr` date NOT NULL,
  `dead_wr` date NOT NULL,
  PRIMARY KEY (`id_wr`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Structure de la table `writer_lang`
--

CREATE TABLE IF NOT EXISTS `writer_lang` (
  `writer_id_wr` int(11) unsigned NOT NULL,
  `lang_id_la` int(3) unsigned NOT NULL,
  `type_wl` char(4) NOT NULL,
  `value_wl` text NOT NULL,
  PRIMARY KEY (`writer_id_wr`,`lang_id_la`,`type_wl`),
  KEY `lang_id_la` (`lang_id_la`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE,
  ADD CONSTRAINT `booking_ibfk_3` FOREIGN KEY (`book_copy_id_bc`) REFERENCES `book_copy` (`id_bc`) ON DELETE CASCADE;

--
-- Contraintes pour la table `book_category`
--
ALTER TABLE `book_category`
  ADD CONSTRAINT `book_category_ibfk_1` FOREIGN KEY (`book_id_bo`) REFERENCES `book` (`id_bo`) ON DELETE CASCADE,
  ADD CONSTRAINT `book_category_ibfk_2` FOREIGN KEY (`category_id_ca`) REFERENCES `category` (`id_ca`) ON DELETE CASCADE;

--
-- Contraintes pour la table `book_copy`
--
ALTER TABLE `book_copy`
  ADD CONSTRAINT `book_copy_ibfk_1` FOREIGN KEY (`book_id_bo`) REFERENCES `book` (`id_bo`) ON DELETE CASCADE;

--
-- Contraintes pour la table `book_writer`
--
ALTER TABLE `book_writer`
  ADD CONSTRAINT `book_writer_ibfk_1` FOREIGN KEY (`book_id_bo`) REFERENCES `book` (`id_bo`) ON DELETE CASCADE,
  ADD CONSTRAINT `book_writer_ibfk_2` FOREIGN KEY (`writer_id_wr`) REFERENCES `writer` (`id_wr`) ON DELETE CASCADE;

--
-- Contraintes pour la table `borrowing`
--
ALTER TABLE `borrowing`
  ADD CONSTRAINT `borrowing_ibfk_1` FOREIGN KEY (`book_copy_id_bc`) REFERENCES `book_copy` (`id_bc`) ON DELETE CASCADE,
  ADD CONSTRAINT `borrowing_ibfk_2` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE;

--
-- Contraintes pour la table `category_lang`
--
ALTER TABLE `category_lang`
  ADD CONSTRAINT `category_lang_ibfk_1` FOREIGN KEY (`category_id_ca`) REFERENCES `category` (`id_ca`) ON DELETE CASCADE,
  ADD CONSTRAINT `category_lang_ibfk_2` FOREIGN KEY (`lang_id_la`) REFERENCES `lang` (`id_la`) ON DELETE CASCADE;

--
-- Contraintes pour la table `chat`
--
ALTER TABLE `chat`
  ADD CONSTRAINT `chat_ibfk_1` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE,
  ADD CONSTRAINT `chat_ibfk_2` FOREIGN KEY (`admin_id_ad`) REFERENCES `admin` (`id_ad`) ON DELETE CASCADE;

--
-- Contraintes pour la table `log`
--
ALTER TABLE `log`
  ADD CONSTRAINT `log_ibfk_1` FOREIGN KEY (`admin_id_ad`) REFERENCES `admin` (`id_ad`) ON DELETE CASCADE;

--
-- Contraintes pour la table `newsletter`
--
ALTER TABLE `newsletter`
  ADD CONSTRAINT `newsletter_ibfk_1` FOREIGN KEY (`admin_id_ad`) REFERENCES `admin` (`id_ad`) ON DELETE CASCADE;

--
-- Contraintes pour la table `newsletter_preferency`
--
ALTER TABLE `newsletter_preferency`
  ADD CONSTRAINT `newsletter_preferency_ibfk_1` FOREIGN KEY (`newsletter_preferency_category_id_npc`) REFERENCES `newsletter_preferency_category` (`id_npc`) ON DELETE CASCADE;

--
-- Contraintes pour la table `newsletter_preferency_category_lang`
--
ALTER TABLE `newsletter_preferency_category_lang`
  ADD CONSTRAINT `newsletter_preferency_category_lang_ibfk_1` FOREIGN KEY (`newsletter_preferency_category_id_npc`) REFERENCES `newsletter_preferency_category` (`id_npc`) ON DELETE CASCADE,
  ADD CONSTRAINT `newsletter_preferency_category_lang_ibfk_2` FOREIGN KEY (`lang_id_la`) REFERENCES `lang` (`id_la`) ON DELETE CASCADE;

--
-- Contraintes pour la table `newsletter_preferency_lang`
--
ALTER TABLE `newsletter_preferency_lang`
  ADD CONSTRAINT `newsletter_preferency_lang_ibfk_1` FOREIGN KEY (`newsletter_preferency_id_np`) REFERENCES `newsletter_preferency` (`id_np`) ON DELETE CASCADE,
  ADD CONSTRAINT `newsletter_preferency_lang_ibfk_2` FOREIGN KEY (`lang_id_la`) REFERENCES `lang` (`id_la`) ON DELETE CASCADE;

--
-- Contraintes pour la table `newsletter_send`
--
ALTER TABLE `newsletter_send`
  ADD CONSTRAINT `newsletter_send_ibfk_1` FOREIGN KEY (`newsletter_id_ne`) REFERENCES `newsletter` (`id_ne`) ON DELETE CASCADE,
  ADD CONSTRAINT `newsletter_send_ibfk_2` FOREIGN KEY (`newsletter_subscriber_id_ns`) REFERENCES `newsletter_subscriber` (`id_ns`) ON DELETE CASCADE;

--
-- Contraintes pour la table `newsletter_subscriber`
--
ALTER TABLE `newsletter_subscriber`
  ADD CONSTRAINT `newsletter_subscriber_ibfk_1` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE;

--
-- Contraintes pour la table `newsletter_subscriber_preferency`
--
ALTER TABLE `newsletter_subscriber_preferency`
  ADD CONSTRAINT `newsletter_subscriber_preferency_ibfk_2` FOREIGN KEY (`newsletter_preferency_category_id_npc`) REFERENCES `newsletter_preferency_category` (`id_npc`) ON DELETE CASCADE,
  ADD CONSTRAINT `newsletter_subscriber_preferency_ibfk_3` FOREIGN KEY (`newsletter_subscriber_id_ns`) REFERENCES `newsletter_subscriber` (`id_ns`) ON DELETE CASCADE;

--
-- Contraintes pour la table `page_content`
--
ALTER TABLE `page_content`
  ADD CONSTRAINT `page_content_ibfk_1` FOREIGN KEY (`placement_id_pl`) REFERENCES `placement` (`id_pl`) ON DELETE CASCADE,
  ADD CONSTRAINT `page_content_ibfk_2` FOREIGN KEY (`lang_id_la`) REFERENCES `lang` (`id_la`) ON DELETE CASCADE;

--
-- Contraintes pour la table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`payment_method_id_pm`) REFERENCES `payment_method` (`id_pm`) ON DELETE CASCADE;

--
-- Contraintes pour la table `penalty`
--
ALTER TABLE `penalty`
  ADD CONSTRAINT `penalty_ibfk_1` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE,
  ADD CONSTRAINT `penalty_ibfk_2` FOREIGN KEY (`payment_id_pa`) REFERENCES `payment` (`id_pa`) ON DELETE CASCADE,
  ADD CONSTRAINT `penalty_ibfk_3` FOREIGN KEY (`payment_method_id_pm`) REFERENCES `payment_method` (`id_pm`) ON DELETE CASCADE;

--
-- Contraintes pour la table `preferency_lang`
--
ALTER TABLE `preferency_lang`
  ADD CONSTRAINT `preferency_lang_ibfk_1` FOREIGN KEY (`preferency_id_pr`) REFERENCES `preferency` (`id_pr`) ON DELETE CASCADE,
  ADD CONSTRAINT `preferency_lang_ibfk_2` FOREIGN KEY (`lang_id_la`) REFERENCES `lang` (`id_la`) ON DELETE CASCADE;

--
-- Contraintes pour la table `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `question_ibfk_1` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE,
  ADD CONSTRAINT `question_ibfk_2` FOREIGN KEY (`lang_id_la`) REFERENCES `lang` (`id_la`) ON DELETE CASCADE;

--
-- Contraintes pour la table `reply`
--
ALTER TABLE `reply`
  ADD CONSTRAINT `reply_ibfk_2` FOREIGN KEY (`question_id_qu`) REFERENCES `question` (`id_qu`) ON DELETE CASCADE,
  ADD CONSTRAINT `reply_ibfk_3` FOREIGN KEY (`admin_id_ad`) REFERENCES `admin` (`id_ad`) ON DELETE CASCADE;

--
-- Contraintes pour la table `subscriber_preferency`
--
ALTER TABLE `subscriber_preferency`
  ADD CONSTRAINT `subscriber_preferency_ibfk_1` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE,
  ADD CONSTRAINT `subscriber_preferency_ibfk_2` FOREIGN KEY (`preferency_id_pr`) REFERENCES `preferency` (`id_pr`) ON DELETE CASCADE;

--
-- Contraintes pour la table `subscription`
--
ALTER TABLE `subscription`
  ADD CONSTRAINT `subscription_ibfk_1` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE,
  ADD CONSTRAINT `subscription_ibfk_2` FOREIGN KEY (`payment_id_pa`) REFERENCES `payment` (`id_pa`) ON DELETE CASCADE,
  ADD CONSTRAINT `subscription_ibfk_3` FOREIGN KEY (`payment_method_id_pm`) REFERENCES `payment_method` (`id_pm`) ON DELETE CASCADE;

--
-- Contraintes pour la table `subscription_transfer`
--
ALTER TABLE `subscription_transfer`
  ADD CONSTRAINT `subscription_transfer_ibfk_1` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE,
  ADD CONSTRAINT `subscription_transfer_ibfk_2` FOREIGN KEY (`to_subscriber`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE,
  ADD CONSTRAINT `subscription_transfer_ibfk_3` FOREIGN KEY (`subscription_id_sub`) REFERENCES `subscription` (`id_sub`) ON DELETE CASCADE;

--
-- Contraintes pour la table `suggestion`
--
ALTER TABLE `suggestion`
  ADD CONSTRAINT `suggestion_ibfk_1` FOREIGN KEY (`subscriber_id_su`) REFERENCES `subscriber` (`id_su`) ON DELETE CASCADE;

--
-- Contraintes pour la table `writer_lang`
--
ALTER TABLE `writer_lang`
  ADD CONSTRAINT `writer_lang_ibfk_1` FOREIGN KEY (`writer_id_wr`) REFERENCES `writer` (`id_wr`) ON DELETE CASCADE,
  ADD CONSTRAINT `writer_lang_ibfk_2` FOREIGN KEY (`lang_id_la`) REFERENCES `lang` (`id_la`) ON DELETE CASCADE;
