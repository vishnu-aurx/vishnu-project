window.onload = function () {
    var url = $(location).attr('href');
    url = url.replace(".html", "/jcr:content.pageLoad.json")
    let isAuthor = document.querySelector(".views").dataset.isAuthor;

    console.log(url);
    if ((/false/).test(isAuthor)) {

        $.get(url, function (result, s) {
            console.log(result);
            console.log("s = " + s)

        })
    }
}
document.querySelector('.prop').disabled = true
document.querySelector('.save').disabled = true
document.querySelector('.prop').value = "";

document.querySelector('.fetch').onclick = function () {
    var url = $(location).attr('href');
    url = url.replace(".html", "/jcr:content.fetch.json")
    url = url.replace("?", "?path=" + document.querySelector('.path').value + "&")
    document.querySelector('.prop').disabled = false;
    document.querySelector('.save').disabled = false;


    console.log(url);
    $.get(url, function (result, s) {
        console.log(result);
        if (result.includes("value")) {
            value = result.split("-");
            document.querySelector('.prop').value = value[0];
            document.querySelector(".msg").innerHTML = "<br><h1>" + value[2] + "</h1>"


        } else {
            document.querySelector('.prop').value = "";
            document.querySelector(".msg").innerHTML = "<br><h1>" + result + "</h1>"
        }

    })

}

document.querySelector('.save').onclick = function () {
    var url = $(location).attr('href');
    url = url.replace(".html", "/jcr:content.fetch.json")
    url = url.replace("?", "?path=" + document.querySelector('.path').value + "&" + "btn=save&value=" + document.querySelector('.prop').value + "&")
    document.querySelector('.prop').disabled = false;
    console.log(url);
    $.get(url, function (result, s) {
        console.log(result);
        if (!result.includes("value")) {
             value = result.split("-");
            document.querySelector(".msg").innerHTML = "<br><h1>" + value[2] + "</h1>"
            document.querySelector('.prop').value = "";
        } else {
        }
    })

}

