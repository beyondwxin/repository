;(function($){
  $.extend($.fn, {
    html5video: function(options){
        var defaults = {
                width:300,               //Number型，播放器宽度。
                height:225,             //Number型，播放器高度。
				sdk_version:19,
				time:0,
				times:"0:00"
            };
	    var options = $.extend(defaults, options);
	    return this.each(function() {
			options.width=$(this).find("video").width();
			options.height=$(this).find("video").height();
			$(this).find("video").css("width",options.width+"px").attr("width",options.width);
			if($(this).find("video").attr('data-time') && $(this).find("video").attr('data-time')!=""){
			options.times= $(this).find("video").attr('data-time');
			}
			var datatimeArray = options.times.split(":");
			
			for(var i=0;i<datatimeArray.length;i++){
				if(i==0){
					options.time+=parseInt(datatimeArray[i])*60;
				}else if(i==1){
					options.time+=parseInt(datatimeArray[i]);
				}
			}

			var o=options;
			if(o.sdk_version && o.sdk_version <=15){
				$(this).find("video").attr("height","auto");
			}else{
				$(this).find("video").attr("height",options.height);
			}
			
                var v_container=$(this);
					var fullStr ="";
					if(o.sdk_version && o.sdk_version >=19){
						fullStr = '<i class="iconfont fullscreen fullscreenIn" title="全屏"></i>'+
                        '<i class="iconfont fullscreen fullscreenOut" title="退出全屏" style="display: none;"></i>';
					}
                    //播放器初始化
                    var videoTemplate=''+
                        '<div class="video_controls">'+
                        '<ul class="video_controls_l">'+
                        '<li class="v_play">'+
                        '<i class="iconfont play plays" title="播放"></i>'+
                        '<i class="iconfont play playe" title="暂停"></i>'+
                        '</li>'+
                        '</ul>'+
						'<div class="video_controls_m">'+
						'<em class="time_cur">0:00</em>'+
						'<div class="video_range">'+
						'<div class="v_progress_n"><div class="curing"></div><div class="loaded"></div><a href="#" class="v_slider"></a></div>'+
                        '<input type="range" class="v_range" value="0" min="0" max="'+options.time+'" />'+
                        '</div>'+
						'<em class="time_end">'+ options.times +'</em>'+
						'</div>'+
                        '<div class="video_controls_r">'+
                        fullStr +
                        '</div>'+
                        '</div>'+
                        '<div class="v_bigplaybtn"><i class="iconfont"></i></div>'+
						'<div class="v_waiting"></div>'+
						'<div class="v_error"><div class="v_error_t">网络不给力呀</div><div class="v_error_bg"></div></div>'+
                        '<div class="video_mask"></div>';
                    v_container.find(".video_body").html(videoTemplate);
                    var videos=v_container.find("video");
                    if(o.loop){
                        videos.attr("loop","loop");
                    }
                    if(o.preload){
                        videos.attr("preload","preload");
                    }
					
					var playingTime = 0,playfirst=true,loadedmetadata=false;

                    //样式初始化
                    v_container.css({width:o.width,height:o.height});
                    v_container.find(".video_body").css({width:'100%',height:'100%'});
					
                    //进度滑块
                    v_container.find(".v_range").on("change",function(e) {
                        $(this).prev(".v_progress").val($(this).val());
						videos.get(0).currentTime=$(this).val();
						v_container.find(".video_range .v_slider").css("left",($(this).val()/options.time)*100+"%");
                    });
					
                    //Play按钮
					 v_container.find(".iconfont.plays").on("click",function(e){
                        $(this).hide().siblings(".play").show();
                        if(videos.get(0).paused){
                            v_container.find(".v_bigplaybtn").hide();
                            videos.get(0).play();
                        }
                    });
					
					 v_container.find(".iconfont.playe").on("click",function(e){
                        $(this).hide().siblings(".play").show();
                        if(!videos.get(0).paused){
                            v_container.find(".v_bigplaybtn").show().css("opacity",".8");
                            videos.get(0).pause();
                        }
                    });
					
					function setDurTime(){
						var _t1=videos.get(0).duration;
						var _t2=Math.floor(_t1/60)+":"+Math.floor(_t1-Math.floor(_t1/60)*60);
						v_container.find(".time_end").html(_t2);
						v_container.find(".video_range .v_range").attr("max",Math.floor(_t1));
						options.time = Math.floor(_t1);
					}
					
                    //video事件绑定
                    videos.on({
                        "loadedmetadata":function(){
                            //视频总时间
							setDurTime();
							loadedmetadata = true;
                        }
                        ,"timeupdate":function(){
                            //当前播放时间
                            var _t1=videos.get(0).currentTime;
                            var _t2=Math.floor(_t1/60)+":"+Math.floor(_t1-Math.floor(_t1/60)*60);
                            v_container.find(".time_cur").html(_t2);
                            v_container.find(".video_range .v_range").attr("value",Math.floor(_t1));
							v_container.find(".video_range .curing").css("width",(Math.floor(_t1)/options.time)*100+"%");
							var rate = Math.floor(_t1)/options.time;
							if(rate>1){
								rate = 1;
							}
							v_container.find(".video_range .v_slider").css("left",rate*100+"%");
							playingTime++;
                        }
						,"error":function(){
							if(videos.get(0).paused){
								v_container.find(".v_error").show();
							}
                        }
                        ,"waiting":function(){
							v_container.find(".v_waiting").css("opacity","1");
							v_container.find(".v_error").hide();
                        }
                        ,"canplay":function(){
							v_container.find(".v_error").hide();
                        }
						,"progress":function(){
							 loadedmetadata = false;
							 v_container.find(".v_waiting").css("opacity","0");
                        }
                        ,"playing":function(){
						$("#staus_text_playing").html("playing");
						if(o.sdk_version && o.sdk_version < 19){
							if(playfirst){
								videos.attr("controls","controls");
								setTimeout(function(){
								videos.removeAttr("controls");
								setDurTime();
								if(o.sdk_version <= 15){
									v_container.css({height:videos.height()});
								}
								},2500);
								playfirst=false;
								}else{
								setTimeout(function(){
								setDurTime();
								},2500);
								}
							}
							if(!loadedmetadata){
							v_container.find(".v_waiting").css("opacity","0");
							}
							v_container.find(".v_error").hide();
                        }
                        ,"canplaythrough":function(){
							v_container.find(".v_error").hide();
                        }
                        ,"ended":function(){
                            v_container.find(".iconfont.playe").hide().siblings(".plays").show();
                            v_container.find(".v_bigplaybtn").show();
                            v_container.find(".video_controls").show();
                            v_container.find(".video_range").show();
                        }
                        ,"play":function(){
                            hideContainer();
                        }
                        ,"durationchange":function(){
                        }
                        ,"pause":function(){
                            showContainer();
							v_container.find(".v_waiting").css("opacity","0");
                        }
						,"loadstart":function(){
						  v_container.find(".v_error").hide();
                        }
                    });

                    //播放按钮Big
                    v_container.find(".v_bigplaybtn").on("click",function(){
					 if(videos.get(0).paused){
						 v_container.find(".iconfont.plays").click();
						 $(this).hide();
					 }
                    });
					
					 v_container.find(".video_mask").on("click",function(){
                         showContainer();
                    });
					
					v_container.find(".video_controls").on("touchmove",function(){
                         playingTime = 0;
                    });					
					
                     //全屏按钮
					var videoH,videoW,videoHO,isInFull=false;
                    v_container.find(".iconfont.fullscreen").on("click",function(){
                        $(this).hide().siblings(".fullscreen").show();
                        if($(this).index()==0){
							isInFull=true;
							videoHO = videos.height();
							$("body").addClass("v_fullscreen");
                            $(".wrapper").removeAttr("style");
							v_container.removeAttr("style");
							videos.removeAttr("style").attr({"width":"100%","height":"auto"});
							videoH = videos.height();
							videoW = videos.width();
							videos.css("margin-top",-videoH/2+"px");
							window.addEventListener("deviceorientation", function(event) {
								if(isInFull){
								
								videoH = videos.height();
								if(window.screen.height < window.screen.width ){
								
									if(videoH < window.screen.height){
									videos.css("margin-top",-videoH/2+"px");
									}else{
									videos.css("margin-top","0");
									}
								}else{
									videos.css("margin-top",-videoH/2+"px");
								}
								}
							}, true);
							jsObj.fullScreenMode();
					   }
                        else{
							isInFull=false;
							window.removeEventListener("deviceorientation", function(event) {}, false);
							if(window.screen.height > window.screen.width ){
							var videoHN = videos.height();
							var videoWN = videos.width();
								if(videoHN!=videoH && videoWN == videoW){
								videoHO=videoHN;
								}
							}
							$("body").removeClass("v_fullscreen");
							$(".wrapper").attr("style","opacity:1;");
							v_container.css({"width":o.width,"height":videoHO});
							videos.attr({"width":o.width,"height":videoHO}).css({"width":o.width});;
							videos.css("margin-top","0");
							jsObj.windowScreenMode();
                        }
                    });
					
					

				//显示控制栏
                function showContainer(){
                    v_container.find(".video_controls").css("display","block");
					playingTime=0;
					var playCheck=setInterval(function(){
						 if(!videos.get(0).paused && v_container.find(".v_bigplaybtn:visible").length < 1 && playingTime > 10 ){
						 hideContainer();
						 clearInterval(playCheck);
					}
					},1800);
                }
                //隐藏控制栏
                function hideContainer(){
                    setTimeout(function(){
                        v_container.find(".video_controls").css("display","none");
                    },700);
                }
				
	   })
    }
  })
})(Zepto)