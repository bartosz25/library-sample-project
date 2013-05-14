function reconnect(server)
{
    server.connect();
}

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

function disconnected(data)
{
    $('#connState').html("Déconnecté");
    $('#reconnect').show();
}
function connected()
{
    $('#connState').html("Connecté");
    $('#reconnect').hide();
}