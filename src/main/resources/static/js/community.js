function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId,1,content)

}

function collapseComments(e) {
    var id=e.getAttribute("data-id");
    var comment=$("#comment-"+id)

    var collapse =e.getAttribute("data-collapse");
    if(collapse){
        comment.removeClass("in");
        e.removeAttribute("data-collapse")
        e.classList.remove("active")
    }else {
        var subCommentContainer =$("#comment-"+id)
        if(subCommentContainer.children().length !=1){
            comment.addClass("in")
            e.setAttribute("data-collapse","in")
            e.classList.add("active")
        }else {
            $.getJSON("/comment/"+id,function (data) {

                $.each(data.data.reverse(),function (index,comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                    })));
                    //
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    //
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    // var c =$(
                    //     "<div>",{
                    //         "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    //         html:comment.content
                    //     });
                    subCommentContainer.prepend(commentElement);
                });

                comment.addClass("in")
                e.setAttribute("data-collapse","in")
                e.classList.add("active")
            })
        }



    }

}

function comment2target(targetId,type,content) {
    if(content==""||content==null){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            console.log(response);
            if(response.code==200){
                window.location.reload();
            }else {
                if(response.code==2003){
                    var isAccepted=confirm(response.message)
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=f94c6eee607885900a9f&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closable",true);
                    }
                }else {
                    alert(response.message)
                }

            }
        },
        dataType: "json"
    });
}
function comment(e) {
    var commentid=e.getAttribute("data-id")
    var content=$("#input-"+commentid).val()
    alert(commentid + content)
    comment2target(commentid,2,content)
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}