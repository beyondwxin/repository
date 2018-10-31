package com.newgen.fragment.ww;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.newgen.UI.CircleImageView;
import com.newgen.domain.Member;
import com.newgen.tools.DataCleanManager;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.user.LoginActivity;
import com.newgen.xj_app.user.SettingActivity;
import com.newgen.xj_app.user.SuggestionActivity;
import com.newgen.xj_app.user.UpdateUserInfoActivity;
import com.newgen.xj_app.ww.AboutUsActivity;
import com.newgen.xj_app.ww.CollectListActivity;
import com.newgen.xj_app.ww.MessageActivity;
import com.newgen.xj_app.ww.TextSizeActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import cn.net.newgen.widget.dialog.ArtAlertDialog;

public class PersonalFragment extends Fragment implements OnClickListener {

    Dialog dialog;
    private View mView;
    private CircleImageView loginButton;
    private TextView user_name;
    private ImageView my_message, my_collect;
    private ImageView user_setting;
    private RelativeLayout my_subscription, my_active, my_suggestion, my_live, my_complain;
    int LoginCode = 1;
    ImageLoader loader;
    DisplayImageOptions options;
    Handler myHandler;

    RelativeLayout rlYeJianMoShi;//夜间模式
    ImageView ivYeJianMoShi;//夜间模式那个图片
    RelativeLayout rlXiaoXiTuiSong;//消息推送
    ImageView ivXiaoXiTuiSong;//消息推送那个图片
    RelativeLayout rlAboutUs;//关于我们
    TextView tvCacheSize;//缓存大小
    TextView tvVersionName;//版本名称
    RelativeLayout rlCleanCache;
    RelativeLayout rlFoutSize;
    RelativeLayout rlVersionW;
    boolean isNight, isTuiSong;//是否夜间 是否推送

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_personal, container, false);

        PublicValue.USER = Tools.getUserInfo(getActivity());

        initWight();
        initListener();
        initImageLoadAndDisplayImageOptions();


        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        PublicValue.USER = Tools.getUserInfo(getActivity());
        checkUserIsLogin();
    }


    private void initWight() {
        // TODO Auto-generated method stub
        loginButton = (CircleImageView) mView.findViewById(R.id.loginButton);
        user_name = (TextView) mView.findViewById(R.id.user_name);
        my_collect = (ImageView) mView.findViewById(R.id.my_collect);
        my_message = (ImageView) mView.findViewById(R.id.my_message);
        user_setting = (ImageView) mView.findViewById(R.id.user_setting);
//        my_subscription = (RelativeLayout) mView.findViewById(R.id.my_subscription);
//        my_active = (RelativeLayout) mView.findViewById(R.id.my_active);
        my_suggestion = (RelativeLayout) mView.findViewById(R.id.my_suggestion);
//        my_live = (RelativeLayout) mView.findViewById(R.id.my_live);
//        my_complain = (RelativeLayout) mView.findViewById(R.id.my_complain);

        rlVersionW = mView.findViewById(R.id.rl_version_w);

        rlYeJianMoShi = mView.findViewById(R.id.rl_ye_jian_mo_shi);
        ivYeJianMoShi = mView.findViewById(R.id.iv_ye_jian_mo_shi);
        rlXiaoXiTuiSong = mView.findViewById(R.id.rl_xiao_xi_tui_song);
        ivXiaoXiTuiSong = mView.findViewById(R.id.iv_xiao_xi_tui_song);
        rlAboutUs = mView.findViewById(R.id.rl_about_us);
        tvCacheSize = mView.findViewById(R.id.tv_cache_size);
        tvVersionName = mView.findViewById(R.id.tv_version_name);
        rlCleanCache = mView.findViewById(R.id.rl_clean_cache);
        rlFoutSize = mView.findViewById(R.id.rl_fout_size);
        rlVersionW = mView.findViewById(R.id.rl_version_w);

        try {
            tvCacheSize.setText(DataCleanManager.getCacheSize(getActivity().getExternalCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvVersionName.setText("v"+Tools.getVision(getActivity()));
    }


    private void initListener() {
        // TODO Auto-generated method stub
        loginButton.setOnClickListener(new OnClick());
        my_collect.setOnClickListener(new OnClick());
        my_message.setOnClickListener(new OnClick());
        user_setting.setOnClickListener(new OnClick());
//        my_subscription.setOnClickListener(new OnClick());
//        my_active.setOnClickListener(new OnClick());
        my_suggestion.setOnClickListener(new OnClick());
//        my_live.setOnClickListener(new OnClick());
//        my_complain.setOnClickListener(new OnClick());
        rlYeJianMoShi.setOnClickListener(this);
        rlXiaoXiTuiSong.setOnClickListener(this);
        rlAboutUs.setOnClickListener(this);
        rlCleanCache.setOnClickListener(this);
        rlFoutSize.setOnClickListener(this);
        rlVersionW.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_ye_jian_mo_shi) {
            isNight = !isNight;
            if (isNight) {
                ivYeJianMoShi.setImageResource(R.drawable.tuoyuan03);
                changeAppBrightness(30);
            } else {
                ivYeJianMoShi.setImageResource(R.drawable.tuoyuan3);
                changeAppBrightness(-1);
            }
        } else if (view.getId() == R.id.rl_xiao_xi_tui_song) {
            isTuiSong = !isTuiSong;
            if (isTuiSong) {
                PushManager.getInstance().initialize(getActivity().getApplicationContext(), com.newgen.service.PushService.class);
                PushManager.getInstance().registerPushIntentService(getActivity().getApplicationContext(), com.newgen.service.IntentService.class);
                ivXiaoXiTuiSong.setImageResource(R.drawable.tuoyuan03);
                Toast.makeText(getActivity(), "خەۋەر قوزغىتىلدى", Toast.LENGTH_SHORT).show();
            } else {
                PushManager.getInstance().stopService(getActivity());
                ivXiaoXiTuiSong.setImageResource(R.drawable.tuoyuan3);
                Toast.makeText(getActivity(), "بارغاندىكى ئوقتۇرۇشىنى تاقالدى", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.rl_about_us) {
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
        } else if (view.getId() == R.id.rl_clean_cache) {
            // 清理缓存
            dialog = new ArtAlertDialog(getActivity(), "بېكىتىلگەن بۇففىرنى ئۆچۈرۈش . بۇففىرنى ئۆچۈرۈش بولامدۇ ?", "بۇفېرلىق ساقلىغۇچ ئېنىقلاش", "ئېنىق ", "ئەمەلدىن قالدۇرماق", new ArtAlertDialog.OnArtClickListener() {

                @Override
                public void okButtonClick() {
                    // 为用户体验效果，先清除部分提示清理完成，后台清除其他缓存
                    DataCleanManager.cleanFiles(getActivity());
                    DataCleanManager.cleanInternalCache(getActivity());
                    DataCleanManager.cleanExternalCache(getActivity());
                    Toast.makeText(getActivity(), "بۇفېرلىق ساقلىغۇچ ئېنىقلاش ، مۇۋەپپەقىيەت ",
                            Toast.LENGTH_SHORT).show();
                    tvCacheSize.setText("0.0 MB");
                }

                @Override
                public void cancelButtonClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (view.getId() == R.id.rl_fout_size) {
            startActivity(new Intent(getActivity(), TextSizeActivity.class));
        }else if(view.getId() == R.id.rl_version_w){
            if (PublicValue.lastVersion != null)
                if (PublicValue.lastVersion.equals(Tools.getVision(getActivity())))
                    Toast.makeText(getActivity(), "نۆۋەتتە ئاللىقاچان ئەڭ يېڭى نەشىرى", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "يېڭى نۇسخىسى يېڭىلاش", Toast.LENGTH_SHORT).show();
        }
    }

    class OnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v == loginButton) {
                if (PublicValue.USER != null) {//进入编辑页面
                    // 用户信息页面
                    Intent intent = new Intent(getActivity(),
                            UpdateUserInfoActivity.class);
                    startActivity(intent);
                } else {//进入登录
                    Intent intent = new Intent(getActivity(),
                            LoginActivity.class);
                    startActivityForResult(intent, LoginCode);
                }
            } else if (v == my_message) {
                Intent intent = new Intent(getActivity(),
                        MessageActivity.class);
                startActivityForResult(intent, LoginCode);
            } else if (v == my_collect) {
                Intent intent = new Intent(getActivity(),
                        CollectListActivity.class);
                startActivity(intent);
            } else if (v == user_setting) {
                Intent intent = new Intent(getActivity(),
                        SettingActivity.class);
                startActivity(intent);
            }
//            else if (v == my_subscription) {
//                Intent intent = new Intent(getActivity(),
//                        ChannelActivity.class);
//                startActivity(intent);
//                getActivity().finish();
//            }

//            else if (v == my_active) {
//                Intent intent = new Intent(getActivity(),
//                        ActiveActivity.class);
//                startActivity(intent);
//            }

            else if (v == my_suggestion) {
                Intent intent = new Intent(getActivity(),
                        SuggestionActivity.class);
                startActivity(intent);
            }

//            else if (v == my_live) {
//                Intent intent = new Intent(getActivity(),
//                        LiveListActivity.class);
//                startActivity(intent);
//            }

//            else if (v == my_complain) {
//                Intent intent = new Intent(getActivity(),
//                        ComplainListActivity.class);
//                startActivity(intent);
//            }
        }

    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据上面发送过去的请求吗来区别  
        switch (requestCode) {
            case 1:
                PublicValue.USER = Tools.getUserInfo(getActivity());
                checkUserIsLogin();
                break;
            default:
                break;
        }
    }


    // 登陆
    public void checkUserIsLogin() {
        Member m = Tools.getUserInfo(getActivity());
        if (m != null) {
            user_name.setText(m.getNickname());
            Tools.log(m.getPhoto());
            loader.displayImage(m.getPhoto(), loginButton, options);
        } else {
            user_name.setText("كىرىش");
            loginButton.setImageResource(R.drawable.user_login_btn);
        }
    }

    private void initImageLoadAndDisplayImageOptions() {
        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.user_login_btn)
                .showImageOnFail(R.drawable.user_login_btn)
                .showImageOnLoading(R.drawable.user_login_btn)
                .resetViewBeforeLoading(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }

    /* 改变App当前Window亮度
     *
     * @param brightness
     */
    public void changeAppBrightness(int brightness) {
        Window window = getActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

}
