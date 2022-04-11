package com.theone.music.ui.fragment;//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.theone.music.app.App;
import com.theone.music.data.model.Music;
import com.theone.music.ui.activity.PlayerActivity;
import com.theone.music.ui.adapter.MusicAdapter;
import com.theone.music.viewmodel.EventViewModel;
import com.theone.mvvm.base.BaseApplication;
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel;
import com.theone.mvvm.ext.qmui.QMUITopBarLayoutExtKt;

import java.util.List;

/**
 * @author The one
 * @date 2022-04-06 17:03
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class BaseMusicFragment<VM extends BaseListViewModel<Music>> extends BasePagerFragment<Music, VM> {

    private EventViewModel mEvent;

    protected EventViewModel getEventVm() {
        if (null == mEvent) {
            mEvent = ((BaseApplication) mActivity.getApplication()).getAppViewModelProvider().get(EventViewModel.class);
        }
        return mEvent;
    }

    /**
     * 更改适配里的当前播放
     *
     * @param music
     */
    private void setCurrentMusic(Music music) {
        if (null != music) {
            ((MusicAdapter) getMAdapter()).setCurrentMusic(music.getShareUrl());
        }
    }

    @Override
    public int getViewModelIndex() {
        return 0;
    }

    @Override
    public void initView(@NonNull View root) {
        super.initView(root);
        setCurrentMusic(mEvent.getPlayMusicLiveData().getValue());
    }

    @Override
    public void onRefreshSuccess(@NonNull List<? extends Music> data) {
        // 这是数据请求成功后的回调
        getMAdapter().getDiffer().submitList((List<Music>) data, () -> {
            setRefreshLayoutEnabled(true);
            getRecyclerView().scrollToPosition(0);
        });
    }

    @NonNull
    @Override
    public BaseQuickAdapter<Music, ?> createAdapter() {
        return new MusicAdapter();
    }

    @Override
    public void createObserver() {
        super.createObserver();
        // 播放通知监听
        getEventVm().getPlayMusicLiveData().observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                //
                setCurrentMusic(music);
            }
        });
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Music music = (Music) adapter.getItem(position);
        PlayerActivity.startPlay(mActivity, music);
    }
}
