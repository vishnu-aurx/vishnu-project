$(".submit-btn").click(function(){
     $(this).closest('.component-report-container').find('.report').html("");
     var resourceType=  $(this).closest('.component-report-container').find('.select-component').val();
    if(resourceType !=null){
     resourceType=resourceType.replace("/apps/", "")
		 let path= $(this).closest('.component-report-container').data("path");
     let id= $(this).closest('.component-report-container').attr("id");
     console.info("path : "+path)
              $.ajax({
                      method: "post",
                      url: path+".componentReport.json",
                       id:id,
                      data: {
                         resourceType: resourceType
                      },
                      success: function (result) {
                       var pagePathObject = $.parseJSON(result);
                     if(pagePathObject.length>0){
                       for (i = 0; i < pagePathObject.length; i++) {
                               $(".component-report-container[id='"+this.id+"']").append("<div class='report' >"+
                                                                                         "<br> <h3> Page Title :"+pagePathObject[i].split("#")[1]+"</h3>"+
                                                                                         "Page : <a  target='_blank' href="+pagePathObject[i].split("#")[0]+".html>"+pagePathObject[i].split("#")[0]+"</a>"+
                                                                                         "</div>")
                       }
                     }else{
                     $(this).closest('.component-report-container').find('.report').html("");
                      setTimeout(function() { alert("Not using this component on any page"); }, 50);
                     }
                         },
                      error: function (error) {
                          console.log("in error");
                      }
                  });
       }
   });

