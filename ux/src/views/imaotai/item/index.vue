<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品ID" prop="iShopId">
        <el-input v-model="queryParams.iShopId" placeholder="请输入商品ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-refresh" size="mini" @click="handleRefresh">刷新i茅台商品</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shopList" border>
      <el-table-column label="商品Id" align="center" min-width="120" prop="itemId" />
      <el-table-column label="商品Code" align="center" min-width="120" prop="itemCode" />
      <el-table-column label="标题" align="center" min-width="220" prop="title" />
      <el-table-column label="图片" align="center" min-width="220" prop="picture" >
        <template slot-scope="scope">
          <img :src="scope.row.picture" style="width: 60px;"/>
        </template>
      </el-table-column>
      <el-table-column label="内容" align="center" min-width="220" prop="content" />
      <el-table-column label="创建时间" align="center" min-width="230" prop="createTime" />
    </el-table>

    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>

<script>
import { getItemListApi, refreshItemApi } from "@/api/imaotai/item";

export default {
  name: "Shop",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      shopList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        iShopId: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      getItemListApi(this.queryParams).then((response) => {
        this.shopList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    reset() {
      this.form = {
        shopId: null,
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleRefresh() {
      refreshItemApi().then(() => {
        this.getList();
        this.$modal.msgSuccess("刷新成功");
      });
    },
  },
};
</script>
