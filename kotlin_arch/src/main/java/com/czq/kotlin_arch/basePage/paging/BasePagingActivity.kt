package com.czq.kotlin_arch.basePage.paging

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.czq.kotlin_arch.R
import com.czq.kotlin_arch.basePage.base.BaseActivity
import com.czq.kotlin_arch.basePage.base.IBasePagingPrensenter
import com.czq.kotlin_arch.basePage.base.IBasePagingView
import com.czq.kotlin_arch.component.cover.CoverFrameLayout
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.activity_base_paging.*
import me.drakeet.multitype.MultiTypeAdapter

abstract class BasePagingActivity<T : IBasePagingPrensenter> : BaseActivity<T>(), IBasePagingView {
    val multiAdapter: MultiTypeAdapter = MultiTypeAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registItemBinder()
    }

    override fun initView() {
        super.initView()
        title = "BasePagingActivity"
        pagingRecycleview.layoutManager = LinearLayoutManager(this)
        pagingRecycleview.adapter = multiAdapter
        showRecyclerViewLoading()
        refreshLayout.setOnRefreshListener {
            mPresenter.resetPage()
            mPresenter.loadData()
        }
        refreshLayout.setOnLoadMoreListener {
            mPresenter.loadData()
        }
        coverLayout?.doReload = {
            refreshLayout.autoRefresh()
        }
    }

    abstract fun registItemBinder()


    override fun getLayoutId(): Int {
        return R.layout.activity_base_paging
    }

    override fun showRecyclerViewEmpty() {
        showEmpty()
    }

    override fun showRecyclerViewError(it: Throwable?) {
        showError(it)
    }

    override fun showRecyclerViewContent() {
        showContent()
    }

    override fun showRecyclerViewLoading() {
        showLoading()
    }

    override fun isRefreshing(): Boolean {
        return refreshLayout.state == RefreshState.Refreshing
    }

    override fun isLoadingMore(): Boolean {
        return refreshLayout.state == RefreshState.Loading
    }


    override fun beginRefresh() {
        refreshLayout.autoRefresh()
    }

    override fun stopRefresh() {
        refreshLayout.finishRefresh()
    }

    override fun stopLoadingdMore(finish: Boolean) {
        if (finish) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            refreshLayout.finishLoadMore()
        }
    }

    override fun setRecyclerViewData(datasource: ArrayList<Any>) {
        multiAdapter.items=datasource
        multiAdapter.notifyDataSetChanged()
    }

}