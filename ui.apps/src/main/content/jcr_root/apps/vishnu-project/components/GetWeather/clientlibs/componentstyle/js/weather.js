 window.onload = function () {

            $.getJSON("http://ip-api.com/json/", function (data) {
                var userIp = data.ip;
                var lat = data.lat;
                var lon = data.lon;
                let weatherContainers = $(".weather-container");
                for(wcIndex = 0; wcIndex < weatherContainers.length; wcIndex++) {
                  let id = weatherContainers.eq(wcIndex).attr("id");
                  let path=weatherContainers.eq(wcIndex).data("path");
                  $.ajax({
                      method: "post",
                      url: path+".weather.json",
                      id: id,
                      data: {
                          userLat: lat,
                          userLon: lon
                      },
                      success: function (result) {
                      try{
                          var obj = $.parseJSON(result);
                          }catch(e){
                          obj = result;
                          }
                          var city = data.city;
                          for (i = 0; i < obj.length; i++) {
                              $(".weather-container[id='"+this.id+"']").append("<div class='weather'><br><h1 class='city weatherData'>city : " + city + "</h1>" +
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


                }
            })
        }