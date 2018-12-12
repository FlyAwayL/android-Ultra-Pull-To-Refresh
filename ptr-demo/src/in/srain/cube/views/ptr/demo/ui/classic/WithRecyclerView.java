package in.srain.cube.views.ptr.demo.ui.classic;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.mints.base.TitleBaseFragment;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestFinishHandler;
import in.srain.cube.views.list.ListViewDataAdapter;
import in.srain.cube.views.list.ViewHolderBase;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.demo.R;
import in.srain.cube.views.ptr.demo.data.DemoRequestData;
import in.srain.cube.views.ptr.demo.ui.MaterialStyleFragment;

public class WithRecyclerView extends TitleBaseFragment {

    private ImageLoader mImageLoader;
    private Adaptor mAdapter;
    private PtrClassicFrameLayout mPtrFrame;

    private int i = 0;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHeaderTitle(R.string.ptr_demo_block_list_view);

        mImageLoader = ImageLoaderFactory.create(getContext());

        final View contentView = inflater.inflate(R.layout.fragment_classic_header_with_recycler_view, null);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.rotate_header_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new Adaptor();
        mAdapter.data.addAll(getData());
        recyclerView.setAdapter(mAdapter);

        mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
        return contentView;
    }

    protected void updateData() {

        DemoRequestData.getImageList(new RequestFinishHandler<JsonData>() {
            @Override
            public void onRequestFinish(final JsonData data) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.data.clear();
                        mAdapter.data.addAll(getData());
                        mPtrFrame.refreshComplete();
                        mAdapter.notifyDataSetChanged();
                    }
                }, 0);
            }
        });
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        int count = i + 5;
        for (; i < count; i++) {
            list.add(i + "");
        }
        return list;
    }

//    private class ViewHolder extends ViewHolderBase<JsonData> {
//
//        private CubeImageView mImageView;
//
//        @Override
//        public View createView(LayoutInflater inflater) {
//            View v = inflater.inflate(R.layout.list_view_item, null);
//            mImageView = (CubeImageView) v.findViewById(R.id.list_view_item_image_view);
//            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            return v;
//        }
//
//        @Override
//        public void showData(int position, JsonData itemData) {
//            mImageView.loadImage(mImageLoader, itemData.optString("pic"));
//        }
//    }

    class Adaptor extends RecyclerView.Adapter<ViewHolder> {

        List<String> data = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }

}