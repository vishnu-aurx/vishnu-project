 window.onload = function () {
            var currentpath = document.querySelector('.currentpath').value;

            var servletUrl = "http://localhost:4502" + currentpath + ".weather.json";

            console.log(servletUrl);

            $.getJSON("http://ip-api.com/json/", function (data) {
                var userIp = data.ip;

                var lat = data.lat;
                var lon = data.lon;
                console.log("lat : " + lat);
                console.log("lon : " + lon);


                var numberOfDays = document.querySelector('.numberOfDays').value;
                console.log("number of days :" + numberOfDays);

                $.ajax({
                    method: "post",
                    url: servletUrl,
                    data: {
                        userLat: lat,
                        userLon: lon,
                        days: numberOfDays
                    },
                    success: function (result) {
                        var obj = $.parseJSON(result);
                        var city = data.city;


                        console.info(obj);

                        for (i = 0; i < obj.length; i++) {

                            console.info("date is " + obj[i].date)
                            $(".weather-container").append("<div class='weather'><br><h1 class='city weatherData'>city : " + city + "</h1>" +
                                "<h1 class='date weatherData'>Date : " + obj[i].date + "</h1>" +
                                "<br><h1 class='temp weatherData'>temp : " + (obj[i].temp-273.15).toPrecision(3) + "</h1>" +
                                "<br><h1 class='wind weatherData'>wind : " + obj[i].wind + "</h1>" +
                                "<br><h1 class='cloud weatherData'>cloud : " + obj[i].cloud + "</h1>" +
                                "<br><img class='icons' src=" + obj[i].icons + "  >"

                            )
                        }
                    },
                    error: function (error) {
                        console.log("in error");
                    }
                });
            })
        }