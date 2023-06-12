  $('.save') .attr('disabled','disabled');
  $('.prop') .attr('disabled','disabled');
  $(".fetch").click(function(){
        var path = $(this).closest('.fetch-container').find('.path').val();
        if(path !=""){
          var invalidPathMsg=$(this).closest('.fetch-container').data("invalid-path-msg")
          var dataFetchMsg=$(this).closest('.fetch-container').data("fetch-path-msg")
          var errorMsg=$(this).closest('.fetch-container').data("error-msg")
          var url = $(location).attr('href');
          url = url.replace(".html", "/jcr:content.fetch.json");
          console.info("this is fetch path : "+path);
          url = url.replace("?", "?path=" + path +"&");
          let id = $(this).closest('.fetch-container').attr("id");
           $.ajax({
                      method: "get",
                      url: url,
                       id:id,

                      success: function (result) {
                       var obj = $.parseJSON(result);
                          console.info(obj);
                         $(".fetch-container[id='"+this.id+"']").find('.massage').html('');
                         $(".fetch-container[id='"+this.id+"']").find(".prop").val(obj.propValue);

                          if(obj.msg==1){
                           $(".fetch-container[id='"+this.id+"']").find('.save') .removeAttr('disabled');
                           $(".fetch-container[id='"+this.id+"']").find('.prop') .removeAttr('disabled');

                          $(".fetch-container[id='"+this.id+"']").append("<div class='massage'><h3>"+dataFetchMsg+"</h3></div>")
                          }else{
                          $(".fetch-container[id='"+this.id+"']").append("<div class='massage'><h3>"+invalidPathMsg+"</h3></div>")
                          }
                         },
                      error: function (error) {
                          console.log("in error");
                          $(".fetch-container[id='"+this.id+"']").append("<div class='massage'><h3>"+errorMsg+"</h3></div>")
                          $(".fetch-container[id='"+this.id+"']").find('.massage').html('');
                     }
                  });

               }

  });

  $(".save").click(function(){
        var path = $(this).closest('.fetch-container').find('.path').val();
        if(path !=""){
          var saveDataMsg=$(this).closest('.fetch-container').data("save-path-msg")
          var invalidPathMsg=$(this).closest('.fetch-container').data("invalid-path-msg")
          var errorMsg=$(this).closest('.fetch-container').data("error-msg")
          var massage=$(this).closest('.fetch-container').data("massage")
          var url = $(location).attr('href');
          url = url.replace(".html", "/jcr:content.save.json")
          console.info("this path :"+path);
          url = url.replace("?", "?path=" + path+ "&value=" + document.querySelector('.prop').value +"&")
          let id= $(this).closest('.fetch-container').attr("id");
             $.ajax({
                      method: "get",
                      url: url,
                       id:id,

                      success: function (result) {
                       var obj = $.parseJSON(result);
                          console.info(obj);
                          $(".fetch-container[id='"+this.id+"']").find('.massage').html('');
                          $(".fetch-container[id='"+this.id+"']").find(".prop").val(obj.propValue);
                          if(obj.msg==1){
                          $(".fetch-container[id='"+this.id+"']").append("<div class='massage'><h3>"+saveDataMsg+"</h3></div>")
                          }else{
                          $(".fetch-container[id='"+this.id+"']").append("<div class='massage'><h3>"+invalidPathMsg+"</h3></div>")
                          }
                         },
                      error: function (error) {
                          $(".fetch-container[id='"+this.id+"']").append("<div class='massage'><h3>"+errorMsg+"</h3></div>")
                          $(".fetch-container[id='"+this.id+"']").find('.massage').html('');

                          console.log("in error");
                      }
                  });
             }

        });