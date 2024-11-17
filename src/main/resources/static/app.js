const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket',
    connectHeaders: {
        Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxIiwic3ViIjoiMSIsImV4cCI6MTczMTkxODg3Nn0.3IXzzVqdb0mMwGDBjKHdzckWrLJnmJb-Na8U35VN1Cs"
    }
});

let currentChat = null
stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
};

function chat(id) {
    currentChat = id
    activateChat(id, true);
    stompClient.subscribe(`/topic/chat/${id}`, (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    }, {
        name: "kim"
    });
}

function exit(id) {
    currentChat = id
    activateChat(id, false);
    stompClient.unsubscribe(`/topic/chat/${id}`);
}

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function activateChat(id, activated) {
    $(`#chat${id}`).prop("disabled", activated);
    $(`#exit${id}`).prop("disabled", !activated);
    if (activated) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    console.log("send name")
    stompClient.publish({
        destination: `/app/chat/${currentChat}`,
        body: JSON.stringify({'name': $("#name").val()})
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#chat1").click(() => chat(1));
    $("#chat2").click(() => chat(2));
    $("#exit1").click(() => exit(1));
    $("#exit2").click(() => exit(1));
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendName());
});