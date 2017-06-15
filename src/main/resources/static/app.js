var socket = new SockJS('/wall-e-websocket');
var stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/wall-e/chat', function (talkToWallE) {
        showReplyFromWallE(JSON.parse(talkToWallE.body).message);
    });
});
/*
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}
*/
function showReplyFromWallE(message) {
   $("#chat_window_panel").append("<div class=\"row msg_container base_receive\"> <div class=\"col-md-2 col-xs-2 avatar\"><img src=\"Avatar-1.jpg\" class=\"img-responsive\"></div><div class=\"col-md-10 col-xs-10\"><div class=\"messages msg_receive\"><p>"+message+"</p><time datetime=\"2009-11-13T21:00\">Agent • 51 min</time></div></div></div>");
   $(".msg_container_base").stop().animate({ scrollTop: $(".msg_container_base")[0].scrollHeight}, 1000);
}

/*$(function () {
    $("form").on('submit', function (e) {
        alert("nk");
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#btn-chat" ).click(function() { *//*sendName();*//*
        alert("hello");
        $("#chat_window_panel").append("
                           <div class="row msg_container base_sent">
                                <div class="col-md-10 col-xs-10">
                                    <div class="messages msg_sent">
                                        <p>i am narendra</p>
                                        <time datetime="2009-11-13T20:00">Timothy • 51 min</time>
                                    </div>
                                </div>
                                <div class="col-md-2 col-xs-2 avatar">
                                    <img src="Avatar-1.jpg" class=" img-responsive ">
                                </div>
                            </div>
        ");
    });
});*/

$(document).on('click', '.panel-heading span.icon_minim', function (e) {
    var $this = $(this);
    if (!$this.hasClass('panel-collapsed')) {
        $this.parents('.panel').find('.panel-body').slideUp();
        $this.addClass('panel-collapsed');
        $this.removeClass('glyphicon-minus').addClass('glyphicon-plus');
    } else {
        $this.parents('.panel').find('.panel-body').slideDown();
        $this.removeClass('panel-collapsed');
        $this.removeClass('glyphicon-plus').addClass('glyphicon-minus');
    }
});
$(document).on('focus', '.panel-footer input.chat_input', function (e) {
    var $this = $(this);
    if ($('#minim_chat_window').hasClass('panel-collapsed')) {
        $this.parents('.panel').find('.panel-body').slideDown();
        $('#minim_chat_window').removeClass('panel-collapsed');
        $('#minim_chat_window').removeClass('glyphicon-plus').addClass('glyphicon-minus');
    }
});
$(document).on('click', '#new_chat', function (e) {
    var size = $( ".chat-window:last-child" ).css("margin-left");
     size_total = parseInt(size) + 400;
    alert(size_total);
    var clone = $( "#chat_window_1" ).clone().appendTo( ".container" );
    clone.css("margin-left", size_total);
});

$(document).on('click', '.icon_close', function (e) {
    //$(this).parent().parent().parent().parent().remove();
    $( "#chat_window_1" ).remove();
});

$(document).on('click', '#btn-chat', function (e) {

    $("#chat_window_panel").append("<div class=\"row msg_container base_sent\"> <div class=\"col-md-10 col-xs-10\"><div class=\"messages msg_sent\"><p>"+$("#btn-input").val()+"</p><time datetime=\"2009-11-13T20:00\">User • 51 min</time></div> </div><div class=\"col-md-2 col-xs-2 avatar\"><img src=\"Avatar-1.jpg\" class=\"img-responsive\"></div></div>");
    stompClient.send("/app/conversation", {}, JSON.stringify({'message': $("#btn-input").val()}));
    document.getElementById("btn-input").value = "";
    $(".msg_container_base").stop().animate({ scrollTop: $(".msg_container_base")[0].scrollHeight}, 1000);
});
