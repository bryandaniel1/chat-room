/**
 * Clears the input text and hides the email form once the AJAX call is 
 * complete and the DOM is updated
 * @param {type} data
 * @param {type} username
 * @returns {undefined}
 */
function handleAdministratorEmailAjaxCall(data, username) {
    var status = data.status;
    switch (status) {
        case "begin":
            break;
        case "complete":
            break;
        case "success":
            $("textarea[id$=" + username + "emailMessageInput]").val('');
            $("div[id$=" + username + "emailFormDiv]").slideToggle(500);
            $("div[id$=" + username + "userInfo]").slideToggle(500);
            break;
    }
}

/**
 * The ready function sets event listeners for the page.
 */
$(document).ready(function () {

    $("#accordion").accordion();
    $("#newRoomForm").hide();
    $("#newFormButton").click(function () {
        $("#newRoomForm").show();
        $("#newFormButton").hide();
    });
    $("#cancelButton").click(function (e) {
        e.preventDefault();
        $("#newFormButton").show();
        $("#newRoomForm").hide();
    });
    $(".emailFormLink").click(function (e) {
        e.preventDefault();
        $(this).closest('div').slideToggle(500);
        $(this).closest('div').next().slideToggle(500);
    });
    $(".cancelEmailFormButton").click(function () {
        $(this).closest('div').slideToggle(500);
        $(this).closest('div').prev().slideToggle(500);
    });
    $('body').addClass("ui-corner-all");
    $('section').addClass("ui-corner-all");
    $('div').addClass("ui-corner-all");
    $('button').button();
    $(".invitationSelection").DataTable();
});

