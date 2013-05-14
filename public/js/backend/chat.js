var newMessages = new Array();
$(document).ready(function() {
    log('Connecting...');

    $('.saveButton').live('click', function() {
        var rel = $(this).attr('rel');
        $.post($('#formChat'+rel).attr('action'), $('#formChat'+rel).serialize(),
            function(data)
            {
	            if(data.added == true)
	            {
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
        // var sender = getMessageSender(payload);
        var obj = $.parseJSON(payload);
        var messageCode = '<li>'+obj.message+'</li>';
        var blockId = 'chat'+obj.from;
        var nbMessages = ++newMessages[obj.from];
        if(obj.from == chatKey)
        {
            $('#chat'+obj.to+' ul').append(messageCode);
            newMessages[obj.to] = 0;
            nbMessages = newMessages[obj.to];
            blockId = 'chat'+obj.to;
        }
        else if($('#'+blockId).length > 0)
        {
            $('#'+blockId + ' ul').append(messageCode);
        }
        else
        {
            var formToCopy = $('#defaultChat form');
            var submitBtn = $('#defaultChat form #save');
            formToCopy.attr('id', 'formChat'+obj.from);
            var oldAction = formToCopy.attr('action');
            formToCopy.attr('action', oldAction+'/'+obj.from);
            submitBtn.attr('rel', obj.from);
            var formHTML = $('#defaultChat').html();
            $('#chatBoxes').append('<div id="'+blockId+'" style="border:1px solid black; padding:10px;"><p>Chat avec '+obj.from+'</p><h2></h2><ul>'+messageCode+'</ul><div>'+$('#defaultChat').html()+'</div></div>');
            formToCopy.attr('id', '');
            formToCopy.attr('action', oldAction);
            submitBtn.attr('rel', '');
            newMessages.push(obj.from);
            newMessages[obj.from] = 0;
            nbMessages = 0;
        }
        $('#'+blockId + ' h2').html("Nouveaux messages : <b>" + nbMessages+"</b>");
    });
    Server.connect();
    
    $('#reconnect a').click(function() {
        reconnect(Server);
        return false;
    });
});