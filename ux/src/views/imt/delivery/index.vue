<template>
  <div class="app-container">
    <div class="search-wrap">
      <el-form :model="queryParams" ref="queryFormRef" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
    </div>

    <el-table v-loading="loading" :data="shopList" border width="100%">
      <el-table-column label="省份" prop="provinceName" min-width="100"/>
      <el-table-column label="城市" prop="cityName" min-width="100"/>
      <el-table-column label="地区" prop="districtName" min-width="100"/>
      <el-table-column label="公司名称" prop="tenantName" min-width="150" show-overflow-tooltip/>
      <el-table-column label="预约项目" prop="itemTitle" min-width="150" show-overflow-tooltip/>
      <el-table-column label="投放数量" prop="inventory" min-width="100"/>
      <el-table-column label="可申购数量" prop="count" min-width="100" />
      <el-table-column label="完整地址" prop="fullAddress" min-width="200" show-overflow-tooltip/>
    </el-table>

    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>

<script>
import { getDeliverySearchApi } from "@/api/imt/delivery";

export default {
  name: "IMTDeliveryList",
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      shopList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        provinceName: null,
        cityName: null,
        districtName: null,
        tenantName: null
      },
    };
  },
  methods: {
    getList() {
      getDeliverySearchApi(this.queryParams).then((response) => {
        this.shopList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.$refs.queryFormRef.resetFiled();
    }
  },
};
</script>

<style lang="scss" scoped>
.search-wrap {
  -webkit-box-shadow: 0 0 10px 2px rgba(0,0,0,.05);
  box-shadow: 0 0 10px 2px rgba(0,0,0,.05);
  padding: 12px 12px 0 12px;
  background: #fff;
  margin-bottom: 12px;
}
</style>

