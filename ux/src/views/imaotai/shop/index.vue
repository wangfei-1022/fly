<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品ID" prop="iShopId">
        <el-input v-model="queryParams.iShopId" placeholder="请输入商品ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="省份" prop="provinceName">
        <el-input v-model="queryParams.provinceName" placeholder="请输入省份" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="城市" prop="cityName">
        <el-input v-model="queryParams.cityName" placeholder="请输入城市" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="地区" prop="districtName">
        <el-input v-model="queryParams.districtName" placeholder="请输入地区" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="公司名称" prop="tenantName">
        <el-input v-model="queryParams.tenantName" placeholder="请输入公司名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-refresh" size="mini" @click="handleRefresh">刷新门店</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shopList" border width="100%">
      <el-table-column label="商品ID" align="center" prop="ishopId" min-width="100"/>
      <el-table-column label="省份" align="center" prop="provinceName" min-width="100"/>
      <el-table-column label="城市" align="center" prop="cityName" min-width="100"/>
      <el-table-column label="地区" align="center" prop="districtName" min-width="100"/>
      <el-table-column label="完整地址" align="center" prop="fullAddress" min-width="230" show-overflow-tooltip />
      <el-table-column label="纬度" align="center" prop="lat" min-width="100"/>
      <el-table-column label="经度" align="center" prop="lng" min-width="100"/>
      <el-table-column label="名称" align="center" prop="name" min-width="200" show-overflow-tooltip/>
      <el-table-column label="公司名称" align="center" prop="tenantName" min-width="230" show-overflow-tooltip/>
      <el-table-column label="创建时间" align="center" prop="createTime" min-width="230"/>
    </el-table>

    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>

<script>
import { listShop, refreshShop } from "@/api/imaotai/shop";

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
        provinceName: null,
        cityName: null,
        districtName: null,
        fullAddress: null,
        lat: null,
        lng: null,
        name: null,
        tenantName: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listShop(this.queryParams).then((response) => {
        this.shopList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        shopId: null,
        iShopId: null,
        provinceName: null,
        cityName: null,
        districtName: null,
        fullAddress: null,
        lat: null,
        lng: null,
        name: null,
        tenantName: null,
        createTime: null,
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
      refreshShop().then(() => {
        this.getList();
        this.$modal.msgSuccess("刷新成功");
      });
    },
  },
};
</script>
