var server = null;
var roomName = null;
var username = null;
var chatRole = null;
var exitSubstring = null;
var chatSubstring = null;
var enterSubstring = null;
var hostUsername = null;

/**
 * This function defines the WebSocket server and the functions for handling the 
 * onopen, onmessage, onerror, and onclose events.
 * @returns {undefined}
 */
function init() {

    roomName = $("#roomName").val();
    username = $("#username").val();
    chatRole = $("#chatRole").val();
    exitSubstring = $("#exitSubstring").val();
    chatSubstring = $("#chatSubstring").val();
    enterSubstring = $("#enterSubstring").val();
    hostUsername = $("#hostUsername").val();
    server = new WebSocket("wss://localhost:8484/ChatRoom/chatroom/"
            + roomName + "/" + username + "/" + chatRole);

    // no operation on open
    server.onopen = function (event) {
    };
    
    server.onmessage = function (event) {
        var returnToLobby = false;
        var message = JSON.parse(event.data);
        var isPicture = message.image;
        var isVideo = message.video;
        var content = message.message;
        var contentSplit = content.split(" ");
        var lineBreakPattern = /<br\s*\/?>/i;
        var users = $("#users").html().split(lineBreakPattern);
        for (var i = 0; i < users.length; i++) {
            users[i] = users[i].trim();
        }

        // processing a user entrance
        if (contentSplit[0] === enterSubstring) {
            if ((users.indexOf(username) === -1) || (users.indexOf(contentSplit[1]) === -1)) {
                $("#users").append(contentSplit[1] + "<br>");
            }
            appendStatusMessageToChatLog(contentSplit.slice(1).join(" "));
        }

        // processing a user exit
        if (contentSplit[0] === exitSubstring) {

            // The exiting user is returned to the lobby.
            if (contentSplit[1] === username) {
                returnToLobby = true;
            } else if (contentSplit[1] === hostUsername) {
                closeChatRoom();
            }

            // The username is removed from the user list            
            for (var u = 0; u < users.length; u++) {
                if (contentSplit[1] === (users[u])) {
                    users.splice(u, 1);
                    break;
                }
            }
            $("#users").html("");
            $("#users").html(users.join("<br>"));
            appendStatusMessageToChatLog(contentSplit.slice(1).join(" "));
        }

        // processing a chat message
        if (contentSplit[0] === chatSubstring) {

            var messageFromSelf = false;
            if (contentSplit[1] === username + ":") {
                messageFromSelf = true;
            }

            if (isPicture) {
                var imageContent = contentSplit[2];
                var href = "/ChatRoom/" + imageContent;
                var src = href;
                appendPictureToChatLog(contentSplit[1], href, src, messageFromSelf);
            } else if (isVideo) {
                var videoContent = contentSplit[2];
                var src = "/ChatRoom/" + videoContent;
                appendVideoToChatLog(contentSplit[1], src, messageFromSelf);
            } else {
                appendChatMessageToChatLog(contentSplit[1], contentSplit.slice(2).join(" "), messageFromSelf);
            }
        }

        if (returnToLobby) {
            sendUserToLobby();
        }
    };
    
    server.onerror = function (event) {
        alert("Error: " + evt.data);
    };

    // no operation on close
    server.onclose = function (event) {
    };
}

/**
 * This function sends a message from the user to the server.
 * @returns {undefined}
 */
function send() {

    $("#errorMessages").empty();
    var newMessage = $("#messageArea").val();
    if (newMessage.length === 0) {
        $("#errorMessages").html("<ul class=\"ui-state-error\"><li>Nothing was entered for the message.</li></ul>");
    } else {
        server.send(chatSubstring + newMessage);
        $("#messageArea").val("");
    }
}

/**
 * This function sends an exit notification to the server
 * @returns {undefined}
 */
function disconnect() {

    if ((server !== null) && (server.readyState !== WebSocket.CLOSED)
            && (server.readyState !== WebSocket.CLOSING)) {
        server.send(exitSubstring);
    }
}

/**
 * This function appends status messages to the chat log area.
 * @param {type} newMessage
 * @returns {undefined}
 */
function appendStatusMessageToChatLog(newMessage) {

    var p = document.createElement('p');
    $(p).append(newMessage);
    $("#chatLog").append(p);
    $("#chatLogContainer").scrollTop($('#chatLog').height());
}

/**
 * This function appends chat messages to the chat log area.
 * @param {type} user
 * @param {type} newMessage
 * @param {type} messageFromSelf
 * @returns {undefined}
 */
function appendChatMessageToChatLog(user, newMessage, messageFromSelf) {

    var p = document.createElement('p');
    var span = document.createElement('span');

    if (messageFromSelf) {
        $(span).addClass('selfMessage ui-corner-all');
    } else {
        $(span).addClass('otherUserMessage ui-corner-all');
    }

    $(span).append(user);
    $(p).append(span).append(" ").append(newMessage);
    $("#chatLog").append(p);
    $("#chatLogContainer").scrollTop($('#chatLog').height());
}

/**
 * This method creates and displays a link with an image from a user.
 * @param {type} user
 * @param {type} href
 * @param {type} src
 * @param {type} messageFromSelf
 * @returns {undefined}
 */
function appendPictureToChatLog(user, href, src, messageFromSelf) {

    var p = document.createElement('p');
    var span = document.createElement('span');
    var a = document.createElement('a');
    $(a).attr('href', href).attr('target', '_blank');
    var img = document.createElement('img');
    $(img).attr('src', src).attr('height', '200').attr('width', '240').appendTo($(a));

    if (messageFromSelf) {
        $(span).addClass('selfMessage ui-corner-all');
    } else {
        $(span).addClass('otherUserMessage ui-corner-all');
    }

    $(span).append(user);
    $(p).append(span).append(" ").append(a);
    $("#chatLog").append(p);
    $("#chatLogContainer").scrollTop($('#chatLog').height());
}

/**
 * This method creates and displays a video element containing a video from a user.
 * @param {type} user
 * @param {type} src
 * @param {type} messageFromSelf
 * @returns {undefined}
 */
function appendVideoToChatLog(user, src, messageFromSelf) {

    var p = document.createElement('p');
    var span = document.createElement('span');
    var video = document.createElement('video');
    $(video).attr('width', '300').attr('controls', 'controls');
    var source1 = document.createElement('source');
    var source2 = document.createElement('source');
    var source3 = document.createElement('source');
    $(source1).attr('src', src).attr('type', 'video/mp4').appendTo($(video));
    $(source2).attr('src', src).attr('type', 'video/webm').appendTo($(video));
    $(source3).attr('src', src).attr('type', 'video/ogg').appendTo($(video));

    if (messageFromSelf) {
        $(span).addClass('selfMessage ui-corner-all');
    } else {
        $(span).addClass('otherUserMessage ui-corner-all');
    }

    $(span).append(user);
    $(p).append(span).append(" ").append(video);
    $("#chatLog").append(p);
    $("#chatLogContainer").scrollTop($('#chatLog').height());
}

/**
 * This method returns the user to the lobby.
 * @returns {undefined}
 */
function sendUserToLobby() {

    disconnect();
    window.location.replace("http://localhost:8080/ChatRoom/lobby");
}

/**
 * This method closes the chat room.
 * @returns {undefined}
 */
function closeChatRoom() {

    $("#messages").hide();
    $("#buttonsPanel").hide();
    $("#uploadPanel").html("<button id=\"return\" class=\"ui-button ui-corner-all\">Return to Lobby</button>");
    $("#return").click(function () {
        sendUserToLobby();
    });
}

/**
 * The ready function sets event listeners for the page. 
 */
$(document).ready(function () {

    init();

    //sending the message on 'enter' key press
    $("#messages").on('keydown ', function (event) {
        if (event.which === 13) {
            send();
            event.preventDefault();
        }
    });
    $("#sendMessage").click(function () {
        send();
    });

    // styling
    $('body').addClass("ui-corner-all");
    $('section').addClass("ui-corner-all");
    $('div').addClass("ui-corner-all");
    $('button').button();

    // show-users toggle button
    $("#showUsers").click(function () {
        $("#usernameSection").slideToggle(500);
        $("#chatLogSection").slideToggle(500);
        if ($("#showUsers").text() === "Show Users") {
            $("#showUsers").text("Hide Users");
        } else {
            $("#showUsers").text("Show Users");
        }
    });

    // handling resize events
    $(window).resize(function () {
        if ($(window).width() > 767) {
            $("#showUsers").text("Show Users");
            $("#showUsers").hide();
            $("#usernameSection").show();
            $("#chatLogSection").show();
        } else {
            $("#showUsers").text("Show Users");
            $("#showUsers").show();
            $("#usernameSection").hide();
            $("#chatLogSection").show();
        }
    });

    // disconnects
    $("#disconnect").click(function () {
        disconnect();
    });
    $(window).on("beforeunload", function () {
        disconnect();
        server.close();
    });
});