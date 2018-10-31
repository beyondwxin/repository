//var _json = null;
/*
 * detail JavaScript
 * write by liulei 20141007
 */
/* 文档加载完毕后 */

var isFirstReadNews = true;

$(document).ready(function() {
	docReady();
	var json = JSON.parse($("#data").val());
	initHtml(json, 12, true, "", null, null);
});

function loadMore() {
	jsObj.loadMore();

}

function initHtml(json, fontSize, supported, serverurl, member, reviewIds) {
	renderHtml(json, {
		ret: 1
	}, supported, serverurl, reviewIds, member);

	setFontSize(fontSize);
}

//阻止浏览器的默认行为
function stopDefault(e) {
	//阻止默认浏览器动作(W3C)
	if(e && e.preventDefault)
		e.preventDefault();
	//IE中阻止函数器默认动作的方式
	else
		window.event.returnValue = false;
	return false;
}
/** 
 * 对日期进行格式化，
 * @param date 要格式化的日期
 * @param format 进行格式化的模式字符串
 *     支持的模式字母有：
 *     y:年,
 *     M:年中的月份(1-12),
 *     d:月份中的天(1-31),
 *     h:小时(0-23),
 *     m:分(0-59),
 *     s:秒(0-59),
 *     S:毫秒(0-999),
 *     q:季度(1-4)
 * @return String
 * @author yanis.wang
 * @see	http://yaniswang.com/frontend/2013/02/16/dateformat-performance/
 */
template.helper('dateFormat', function(date, format) {
	date = new Date(parseInt(date) * 1000);
	var map = {
		"M": date.getMonth() + 1, //月份 
		"d": date.getDate(), //日 
		"h": date.getHours(), //小时 
		"m": date.getMinutes(), //分 
		"s": date.getSeconds(), //秒 
		"q": Math.floor((date.getMonth() + 3) / 3), //季度 
		"S": date.getMilliseconds() //毫秒 
	};
	format = format.replace(/([yMdhmsqS])+/g, function(all, t) {
		var v = map[t];
		if(v !== undefined) {
			if(all.length > 1) {
				v = '0' + v;
				v = v.substr(v.length - 2);
			}
			return v;
		} else if(t === 'y') {
			return(date.getFullYear() + '').substr(4 - all.length);
		}
		return all;
	});
	return format;
});

template.helper('dateFormatPublish', function(dateStr) {
	return dateStr.substring(0, 16);
});

template.helper('dateFormatComment', function(date, format) {
	var unix_time = get_unix_time(date);
	date = new Date(parseInt(unix_time) * 1000);
	var dateTime = "";
	var minute = 1000 * 60;
	var hour = minute * 60;
	var day = hour * 24;
	var halfamonth = day * 15;
	var month = day * 30;

	function getDateDiff(dateTimeStamp) {
		var now = new Date().getTime();
		var diffValue = now - dateTimeStamp;
		var monthC = diffValue / month;
		var weekC = diffValue / (7 * day);
		var dayC = diffValue / day;
		var hourC = diffValue / hour;
		var minC = diffValue / minute;
		if(monthC >= 1) {
			result = (date.getFullYear() + '').substr(2) + " / " + pad(date.getMonth() + 1, 2) + " / " + pad(date.getDate(), 2);
		} else if(weekC >= 1) {
			result = parseInt(weekC) + "周前";
		} else if(dayC >= 1) {
			result = parseInt(dayC) + "天前";
		} else if(hourC >= 1) {
			result = parseInt(hourC) + "个小时前";
		} else if(minC >= 1) {
			result = parseInt(minC) + "分钟前";
		} else
			result = "刚刚";
		return result;
	}

	dateTime = getDateDiff(date);
	return dateTime;

});

function get_unix_time(dateStr) {
	var newstr = dateStr.replace(/-/g, '/');
	var date = new Date(newstr);
	var time_str = date.getTime().toString();
	return time_str.substr(0, 10);
}

function getDays(strDateStart, strDateEnd) {
	var strSeparator = "-"; //日期分隔符
	var oDate1;
	var oDate2;
	var iDays;
	oDate1 = strDateStart.split(strSeparator);
	oDate2 = strDateEnd.split(strSeparator);
	var strDateS = new Date(oDate1[0] + "-" + oDate1[1] + "-" + oDate1[2]);
	var strDateE = new Date(oDate2[0] + "-" + oDate2[1] + "-" + oDate2[2]);
	iDays = parseInt(Math.abs(strDateS - strDateE) / 1000 / 60 / 60 / 24) //把相差的毫秒数转换为天数 
	return iDays;
}

function pad(num, n) {
	var len = num.toString().length;
	while(len < n) {
		num = "0" + num;
		len++;
	}
	return num;
}

//文档加载完成
function docReady() {

	//所有#链接取消默认动作
	$("body").on("click", "a[href='#']", function(e) {
		stopDefault(e);
	})

	//绑定评论展开事件
	$("body").on("click", ".comment_box .open_all", function(e) {
		if($(this).hasClass("opening")) {
			$(this).text("+ 展开全部楼层").removeClass("opening").parents(".reply_con").find(".reply_item.none").hide();
		} else {
			$(this).text("- 收起楼层").addClass("opening").parents(".reply_con").find(".reply_item").show();
		}
	});

	//绑定评论点赞
	$("body").on("click", ".comment_box .praise", function(e) {
		var num = $(this).text();
		num = Number(num);
		var id = $(this).attr("data-id");
		if(!$(this).hasClass("praised")) {
			//赞
			$(this).addClass("praised");
			$(this).text(num + 1);
			jsObj.commentsupport(Number(id));
		}
	});

	//绑定评论
	$("body").on("click", ".comment_box .reply_btn,.comment_box .con_text a", function(e) {
		var id = $(this).parents("li").attr("data-id");
		jsObj.comment(id);
	});

	//绑定订阅
	$("body").on("click", ".subscribe_btn", function(e) {
		var id = $(this).attr("data-id");
		if(!$(this).hasClass("subscribed")) {
			jsObj.subscribe(id);
			$(this).addClass("subscribed").find(".t").text("已订");
		}

	});

	//绑定订阅细览
	$("body").on("click", "#subscribe_detail", function(e) {
		var id = $(this).attr("data-id");
		var name = $(this).attr("data-name");
		var url = $(this).attr("data-url");
		jsObj.enterSubscribe(id, name, url);

	});

	//绑定关键词
	$("#keywords").on("click", "a", function(e) {
		var id = $(this).attr("data-id");
		jsObj.searchKeyword(id);
	});

	//绑定相关新闻
	$("body").on("click", ".relat_post a,.relat_video li a", function(e) {
		var id = $(this).attr("data-id");
		var tagid = $(this).attr("data-tagid");
		var type = $(this).attr("data-type");
		jsObj.relatedNews(id, tagid, type);
	});

	//绑定图片跳转大图
	$("#htmlDetail,#htmlMedias").on("click", 'img', function(e) {
		if($(this).parent(".poster_warp").length < 1 && $(this).parent("a").length < 1) {
			var url = $(this).attr("src");
			jsObj.showBigPic(url);
		}
	})

	//	$("#htmlSurvey").on("click", "img", function() {
	//		var url = $(this).attr("src");
	//		jsObj.showBigPic(url);
	//	})
}

//绑定视频播放
function videoBind() {
	$("video").each(function() {
		var video = $(this)[0];
		video.addEventListener("playing", function() {
			jsObj.stopAudio();
		});
	})
}

//渲染页面的调用
function renderHtml(json, idData, supported, serverurl, reviewIds, member) {
	if(json && json != null) {
		var article = json.data;
		//渲染新闻标题
		renderHeader(article);
		//渲染导语
		renderIntroduction(article)
		//渲染新闻正文
		renderDetail(article);
		//修正文章正文中的视频
		changeVideo();
		//渲染新闻评论
		renderNewcomment(json, reviewIds);

		var supportData = {
			number: json.data.newsPubExt.supportcount,
			supported: supported
		}
		renderShare(supportData);
		renderHotNews(json);
		bindHotNews();
	}
	//渲染新闻视频
	if(json.data.listvedio && json.data.listvedio != null) {
		if(idData.sdk_version && idData.sdk_version != "") {
			var sdk_version = idData.sdk_version;
		} else {
			var sdk_version = 20;
		}
		renderMedias({
			mediaList: json.data.listvedio
		}, sdk_version);
	}
	/**渲染投票**/
	if(json.data.listvote && json.data.listvote != null && json.data.listvote.length > 0) {
		var votes = json.data.listvote;
		renderSurvey({
			surveyList: votes,
			serverurl: serverurl,
			member: member
		}, idData.survey_id);
	}

	$("#htmlMedias img, #htmlDetail img").each(function() {
		if($(this).attr("height") && $(this).attr("height") != "" && $(this).attr("width") && $(this).attr("width") != "") {
			var contentW = $("#content").width();
			var heightO = $(this).attr("height");
			var widthO = $(this).attr("width");
			var heightN = heightO * (contentW / widthO);
			$(this).attr("height", heightN);
		}
	});

	setTimeout(function() {
		$("#content").fadeIn("normal").css("opacity", "1");
		jsObj.hideLoading();
	});
}

//修正文章正文中的视频
function changeVideo() {
	if($(".edui-upload-video").length > 0) {
		$(".edui-upload-video").each(function() {
			var video = {};
			video.url = $(this).attr("src");
			video.img = video.url + ".jpg";
			var html = template("tmpVideo", video);
			$(this).before(html);
			$(this).remove();
		});
	}
}

//渲染头部
function renderHeader(data) {
	var html = template('tmpHeader', data);
	$('#htmlHeader').html(html);
}
//渲染导语

function renderIntroduction(data) {
	var html = template('tmpIntroduction', data);
	$('#htmlIntroduction').html(html);
}
//渲染正文

function renderDetail(data) {
	var html = template('tmpDetail', data);
	$('#htmlDetail').html(html);
	$(".detail img").css({
		"width": "100%",
		"height": "auto"
	});
	$(".detail p").css({
		"font-size": "auto"
	});
}
//渲染媒体

function renderMedias(data, sdk_version) {
	var html = template('tmpMedias', data);
	$('#htmlMedias').html(html);
	videoBind();

	$(".poster_warp").each(function() {
		var poster_warp = $(this);
		if(poster_warp.find("img").length > 0) {
			var oImg = $(this).find("img")[0];
			if(oImg.complete) {
				poster_warp.find(".v_bigplaybtn i").css("opacity", "1");
			} else {
				oImg.onload = function() {
					poster_warp.find(".v_bigplaybtn i").css("opacity", "1");
				}
			}
		} else {
			poster_warp.find(".v_bigplaybtn i").css("opacity", "1");
		}
	});

	$(".poster_warp").on("click", function() {
		jsObj.playVideo($(this).attr("data-vsrc"));
		return false;
	});
}

//渲染小调查
function renderSurvey(data, idStr, member) {
	function pvreData(surveyList) {
		var survey_choice_obj_array = surveyList.listVoteItem;
		var allVoteNum = 0;
		for(var j = 0; j < survey_choice_obj_array.length; j++) {
			allVoteNum += parseInt(survey_choice_obj_array[j].count);
		}
		for(var k = 0; k < survey_choice_obj_array.length; k++) {
			var preo_val = 0;
			if(allVoteNum > 0) {
				preo_val = parseInt((parseFloat(survey_choice_obj_array[k].count) / allVoteNum) * 100);
			}
			surveyList.listVoteItem[k]["preo_val"] = preo_val;

		}
		return surveyList;
	}

	for(var i = 0; i < data.surveyList.length; i++) {
		var isPolled = data.surveyList[i].isvoteflag == 1 //为1时表示可以投票
		data.surveyList[i].isPolled = isPolled;
		if(!data.surveyList[i].survey_id || data.surveyList[i].survey_id == null || data.surveyList[i].survey_id == "" || data.surveyList[i].survey_id == "null") {
			data.surveyList[i].survey_id = i;
		}
		if(!isPolled) {
			data.surveyList[i].survey_endtime = 'true';
		} else {
			data.surveyList[i].survey_endtime = 'false';
		}
		if(data.surveyList[i].type == 0) //投票
			data.surveyList[i] = pvreData(data.surveyList[i]);
		else { //问卷
			for(var j = 0; j < data.surveyList[i].listVoteQuestion.length; j++) {
				data.surveyList[i].listVoteQuestion[j] = pvreData(data.surveyList[i].listVoteQuestion[j])
			}
		}
	}
	var html = "";
	if(data.surveyList[0].showType != 1)
		html = template('tmpSurvey', data);
	else
		html = template('vote2', data);
	$('#htmlSurvey').html(html);

	//小调查交互初始化
	/** 单选 **/
	$("#htmlSurvey").on("click", "li input", function() {
		var isChecked = $(this).attr("checked");
		if(isChecked) {
			$(this).parents("li").addClass("on");
			if($(this).attr("type") == "radio") {
				$(this).parents("li").siblings("li").removeClass("on");
			}
		} else {
			$(this).parents("li").removeClass("on");
		}
		var surveyIndex = $(this).attr("data-surveyIndex");
		var choiceIndex = $(this).attr("data-choiceIndex");
		//获取当前投票项的类型
		var survey = data.surveyList[surveyIndex];
		if(survey.type == 1) { //问卷
			var questionIndex = $(this).attr("data-questionIndex");
			var poll_count = parseInt(survey.listVoteQuestion[questionIndex].listVoteItem[choiceIndex].count)
			survey.listVoteQuestion[questionIndex].listVoteItem[choiceIndex].count = poll_count + 1;
		} else { //投票
			var poll_count = parseInt(data.surveyList[surveyIndex].listVoteItem[choiceIndex].count);
			data.surveyList[surveyIndex].listVoteItem[choiceIndex].count = poll_count + 1;
		}
	});
	/** 多选 **/
	$("#htmlSurvey").on("click", ".sub_btn", function() {
		/* 验证验证码 */
		var code = $("#validateCode").val();
		if(!code) {
			$("#validateCode").focus();
			return;
		}
		var surveyIndex = $(this).attr("data-surveyIndex");
		//验证是否有题漏选
		var items = $("#htmlSurvey .items");
		for(var k = 0; k < items.length; k++) {
			var count = $(items[k]).find("li input:checked").length;
			if(count <= 0) {
				window.scrollTo(0, $(items[k]).prev().offset().top);
				alert("请完成第" + (k + 1) + "题");
				return;
			}
		}
		var reslutStr = theReslutStr(data.surveyList[surveyIndex].id);
		if(reslutStr == "") {
			return false;
		}
		rendReslut(data.surveyList[surveyIndex]);
		$(this).hide();
		jsObj.userSurvey("" + reslutStr, code);
	})

	function rendReslut(data) {
		if(data.type == 0) //投票
			data = pvreData(data);
		else { //问卷
			for(var j = 0; j < data.listVoteQuestion.length; j++) {
				data.listVoteQuestion[j] = pvreData(data.listVoteQuestion[j])
			}
		}
		var htmlResult = template('tmpSurveyResult', {
			surveyItem: data
		});
		$("#surveyItem_" + data.id).html(htmlResult);
	}

	function theReslutStr(survey_id) {
		var inputs = $("#surveyItem_" + survey_id + " li input:checked");
		var strArray = [];
		inputs.each(function() {
			var poll_id = $(this).attr("data-poll_id");
			strArray.push(poll_id);
		});
		return strArray.join("##");
	}

}
//渲染详情页评论
function renderNewcomment(data, ids) {
	if(data.data.listreview.length > 0) {
		if(!idStrObj) {
			var idStrObj = {};
			idStrObj.praise = "";
		}
		var dataObj = {
			newcommentsList: data.data.listreview
		};
		var html = template('tmpNewcomments', dataObj);
		$('#htmlNewcomments').html(html);
		/* 标记点过赞的评论 */
		tagSuppered(ids);
	}
}

/* 标记已点过赞的 */
function tagSuppered(ids) {
	if(ids && ids.length > 0) {
		for(var i = 0; i < ids.length; i++) {
			var comment = $(".praise[data-id='" + ids[i] + "']");
			comment.addClass("praised");
		}
	}
}

function commentisPraised(dataList, idStr) {
	if(idStr && idStr != "" && idStr != null) {
		for(var i = 0; i < dataList.length; i++) {
			var isPraised = false;
			var comment_id = dataList[i].comments_items[0].comment_id;
			var strsArray = new Array();
			strsArray = idStr.split(",");
			if($.inArray(String(comment_id), strsArray) > -1) {
				isPraised = true;
			};
			dataList[i].comments_items[0].isPraised = isPraised;
		}
	}
	return dataList;
}

//渲染广告
function renderAdBanner(data) {
	var html = template('tmpAdBanner', data);
	$('#htmlAdBanner').html(html);
	var showTracks = [];
	var clickTracks = [];
	for(var key in data.tracks) {
		if(data.tracks[key].type == "1") {
			showTracks.push(data.tracks[key].url);
		}
		if(data.tracks[key].type == "2") {
			clickTracks.push(data.tracks[key].url);
		}
	}
	showTracks = showTracks.join(",");
	clickTracks = clickTracks.join(",");
	var jumpUrl = data.redirect_url,
		jumpUrlType = data.redirect_type;
	$("#htmlAdBanner").on("touchend", "a", function(e) {
		stopDefault(e);
		jsObj.sendClkData(jumpUrlType, jumpUrl, clickTracks);
	})
	if(typeof(jsObj) === "function") {
		jsObj.sendImpData(showTracks);
	}
}

//字体切换
function setFontSize(size) {
	if(size == 0) {
		$("body").attr("class", "font_big");
	} else if(size == 1) {
		$("body").attr("class", "font_middle");
	} else if(size == 2) {
		$("body").attr("class", "font_min");
	}
}

//评论页渲染评论-最新
function renderNewCommentsHtml(data, isAdd, ids) {
	var data = {
		commentsList: data.listreview
	};
	if(data.commentsList.length > 0)
		$("#loadMore").show();
	var html = template('tmpComments', data);
	if(isAdd && isAdd != "false") {
		if(data.commentsList.length > 0) {
			$("#htmlComments").append(html);
		}
	} else {
		$('#htmlComments').html(html);
	}
	tagSuppered(ids);
}

//获取最新的一个评论的id
function getNewConmentId() {
	var id = $("#htmlComments .comment_box ul > li").last().attr("data-lastesId");
	jsObj.loadMore(id);
}

//渲染订阅
function renderOrg(data, isSubscribed) {
	data.isSubscribed = isSubscribed.toString();
	var html = template('tmpSubscribe', data);
	$('#htmlSubscribe').html(html);
}

//退出全屏
function exitFullSrceen() {
	$(".iconfont.fullscreen").eq(1).click();
}

//唤醒后检查视频状态
function wakeupVideo() {
	if($("video").length > 0) {
		$("video").each(function() {
			if($(this).get(0).paused) {
				var video_container = $(this).parents(".video_container");
				video_container.find(".playe").hide().siblings(".plays").show();
				video_container.find(".video_controls,.v_bigplaybtn").show();
			}
		});
	}
}

//重置订阅状态
function orgStatus(isSubscribed) {
	if(!isSubscribed || isSubscribed == "false") {
		$("#subscribe_btn").removeClass("subscribed").find(".t").text("订阅");
	} else {
		$("#subscribe_btn").addClass("subscribed").find(".t").text("已订");
	}
}

function renderVote(json, serverurl, member) {
	if(json.ret == 1) {
		renderSurvey({
			surveyList: [json.data],
			serverurl: serverurl,
			member: member
		}, "");
		setTimeout(function() {
			$("#content").fadeIn("normal").css("opacity", "1");
			jsObj.hideLoading();
		});
	}
}

function renderShare(json) {
	var html = template('shareModel', json);
	$('#share').html(html);
}

function renderHotNews(json) {
	var data = {
		hotNews: json.recommend
	};
	var html = template("tmpHotNews", data)
	$("#htmlHotNews").html(html);

	/* 设置推荐新闻封面图 */
	$('.faceimg').jqthumb({
		width: 80,
		height: 60,
		after: function(imgObj) {
			imgObj.css('opacity', 0).animate({ opacity: 1 }, 1000);
		}
	});

}

function bindHotNews() {
	$(".hotNewsItem").on("click", function() {
		var newsId = parseInt($(this).attr("data-newsId"));
		var type = parseInt($(this).attr("data-type"));
		var infoType = parseInt($(this).attr("data-infoType"));
		jsObj.showHotNews(newsId, type, infoType);
	});
}

function share(platform) {
	jsObj.share(platform);
}

function support(node) {
	$(node).removeAttr("onclick");
	$(node).attr("src", "imgs/icon_good_ed.png");
	var countStr = $(node).parent().children('p').text();
	var count = parseInt(countStr);
	$(node).parent().children('p').text(count + 1);
	jsObj.support();
}

function speak(){
	if(isFirstReadNews){
		isFirstReadNews = false;
		jsObj.showText("第一次读报执行!");
		var text = $("#htmlDetail").text();
		jsObj.speakDetail(text);
		$("#htmlHeader img").attr("src", "imgs/reading_news.gif"); 
	}else{
		if($("#htmlHeader img")[0].src!="file:///android_asset/detail/imgs/read_news.png"){
			jsObj.showText("现在暂停播放");
			$("#htmlHeader img").attr("src", "imgs/read_news.png"); 
			jsObj.pauseDetail();
		}else{
			jsObj.showText("现在开始播放");
			$("#htmlHeader img").attr("src", "imgs/reading_news.gif"); 
			jsObj.resumeDetail();
		}
	}
	
}
