<template>
  <div class="app-container">
    <div class="search-wrap" v-show="showSearch">
      <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" label-width="68px">
        <el-form-item label="用户" prop="mobile">
          <el-input v-model="queryParams.mobile" placeholder="请输入用户" clearable/>
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker v-model="queryParams.createDateRange" style="width: 340px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="operation-wrap mb8">
      <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete" v-hasPermi="['monitor:operlog:remove']">删除</el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleClean" v-hasPermi="['monitor:operlog:remove']">清空</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </div>

    <el-table v-loading="loading" :data="list" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column label="用户" prop="mobile" width="200" />
      <el-table-column label="日志内容" prop="content" show-overflow-tooltip/>
      <el-table-column label="日志日期" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>

<script>
import { getLogListApi, deleteLogApi, cleanLogApi } from "@/api/imt/log";

export default {
  name: "IMTLogList",
  data() {
    return {
      loading: true,
      ids: [],
      showSearch: true,
      total: 0,
      list: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        createDateRange: [],
        mobile: undefined,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      let data = {
        ...this.queryParams
      }
      if(this.queryParams.createDateRange && this.queryParams.createDateRange.length) {
        data.createTimeStart = this.queryParams.createDateRange[0]
        data.createTimeEnd = this.queryParams.createDateRange[1]
        delete data.createDateRange
      }
      getLogListApi(data).then(
        (res) => {
          this.list = res.data.list;
          this.total = res.data.total;
          this.loading = false;
        }
      );
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.logId);
    },
    handleDelete(row) {
      const logIds = row.logId || this.ids;
      this.$modal
        .confirm('是否确认删除日志编号为"' + logIds + '"的数据项？')
        .then(function () {
          return deleteLogApi(logIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    handleClean() {
      this.$modal
        .confirm("是否确认清空所有操作日志数据项？")
        .then(function () {
          return cleanLogApi();
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("清空成功");
        })
        .catch(() => {});
    },
    handleExport() {
      this.download(
        "monitor/operlog/export",
        {
          ...this.queryParams,
        },
        `operlog_${new Date().getTime()}.xlsx`
      );
    },
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
  overflow: hidden;
}
.operation-wrap{
  overflow: hidden;
}
</style>
