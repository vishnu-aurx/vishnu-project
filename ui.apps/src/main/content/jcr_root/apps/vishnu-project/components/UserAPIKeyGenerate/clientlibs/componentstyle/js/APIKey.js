
  $(".user-submit-btn").click(function(){
          let isAuthor = document.querySelector(".api-key-container").dataset.isAuthor;
      console.info("isAuthor :"+isAuthor);
      if(isAuthor){
        var username = $(this).closest('.api-key-container').find('.username').val();
        var email = $(this).closest('.api-key-container').find('.email').val();
        let id = $(this).closest('.api-key-container').attr("id");

          if(username !='' && email !=''){
        console.info("this is id :"+id);
        console.info("this is username :"+username);
        console.info("this is email :"+email);

      var url ="/bin/api-key.getAPIKey.json?username="+username+"&email="+email+"";
           $.ajax({
                      method: "get",
                      url: url,
                      id :id,
                      success: function (result) {
                         var obj = $.parseJSON(result);
                         console.info(obj.api_Key)
                          $(".api-key-container[id='"+this.id+"']").find('.massage').html('');
                          $(".api-key-container[id='"+this.id+"']").append("<div class='massage'><h3> Your API Key :  "+(obj.api_Key)+"</h3></div>");

                         },
                      error: function (error) {
                          console.log("in error");
                      }
                  });
          }else{
                              setTimeout(function() { alert("please enter the details first")}, 50);
                               }
      }

     var sendMailUrl="/bin/sendemail.email.json?username="+username+"&email="+email+"";

             $.ajax({
                      method: "get",
                      url: sendMailUrl,
                      id :id,
                      success: function (result) {

                         console.info(result)

                         },
                      error: function (error) {
                          console.log("in error");
                      }
                  });

              });


