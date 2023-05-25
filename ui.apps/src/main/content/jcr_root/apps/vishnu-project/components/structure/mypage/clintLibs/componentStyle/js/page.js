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


