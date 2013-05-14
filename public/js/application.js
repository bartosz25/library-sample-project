$(document).ready(function() {
    log('Connecting...');

    $('#save').click(function() {
        $.post($('#chat').attr('action'), $('#chat').serialize(),
            function(data)
            {
	            if(data.added == true)
	            {
                    $('#text').val('');
                    var message = '{'+
                    '"from" : "'+data.from+'",'+
                    '"to" : "'+data.to+'",'+
                    '"message" : "'+data.message+'"'+
                    '}';
                    send(message);
                }
                else
                {
                    alert(data.msg);
                }
            }, "json"
        );
        return false;
    });

    //Let the user know we're connected
    Server.bind('open', function() {
        connected();
    });

    //OH NOES! Disconnection occurred.
    Server.bind('close', function( data ) {
        disconnected(data);
    });

    //Log any messages sent from server
    Server.bind('message', function( payload ) {
        var obj = $.parseJSON(payload);
        log("<p><b>"+obj.from + "</b> : " +obj.message+"</p>");
    });
    Server.connect();
    
    $('#reconnect a').click(function() {
        reconnect(Server);
        return false;
    });
});