<template>
  <div class="app-container">
    <div class="search-wrap">
      <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="queryParams.mobile" placeholder="请输入手机号" clearable @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="mb10">
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddIUser">添加账号</el-button>
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">直接新增</el-button>
      <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </div>

    <el-table v-loading="loading" :data="userList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column label="手机号" prop="mobile" min-width="120"/>
      <el-table-column label="名称" prop="name"/>
      <el-table-column label="预约项目code" prop="itemCode" min-width="120"/>
      <el-table-column label="省份" prop="provinceName">
        <template slot-scope="scope">
          <span>{{ scope.row.provinceName}}{{ scope.row.cityName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" prop="shopType" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.shopType == 1 ? "预约出货量最大门店" : "预约附近门店"}}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约执行分钟" prop="minute" min-width="100"/>
      <el-table-column label="到期时间" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, "{y}-{m}-{d} {h}:{i}:{s}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width" min-width="280">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-thumb" @click="reservationFn(scope.row)">预约</el-button>
          <el-button size="mini" type="text" icon="el-icon-thumb" @click="travelReward(scope.row)">旅行</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-refresh" @click="handleUpdateToken(scope.row)">刷新token</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog title="添加\更新用户" :visible.sync="openUser" width="500px" append-to-body>
      <el-form ref="form" :model="form" size="mini" label-width="90px">
        <el-form-item label="手机号" prop="mobile">
          <el-row>
            <el-col :span="16">
              <el-input v-model="form.mobile" placeholder="请输入I茅台用户手机号"/>
            </el-col>
            <el-col :span="8">
              <el-button type="primary" plain @click="sendCodeFn(form.mobile)" :disabled="state" style="width: 100%;">
                发送验证码
                <span v-if="state">({{ stateNum }})</span>
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="验证码" prop="userId">
          <el-input v-model="form.code" placeholder="请输入验证码"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="loginFn(form.mobile, form.code)">登 录</el-button>
        <el-button size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="refreshToken" width="500px">
      <el-form ref="form" :model="form" size="mini" label-width="90px">
        <el-form-item label="手机号" prop="mobile">
          <el-row>
            <el-col :span="16">
              <el-input v-model="form.mobile" placeholder="请输入I茅台用户手机号"/>
            </el-col>
            <el-col :span="8">
              <el-button type="primary" plain @click="sendCodeFn(form.mobile)" :disabled="state" style="width: 100%;">
                发送验证码
                <span v-if="state">({{ stateNum }})</span>
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="验证码" prop="code">
          <el-input v-model="form.code" placeholder="请输入验证码"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="refresh(form.mobile, form.code, form.deviceId, 1)">刷 新</el-button>
        <el-button size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <edit-user-dialog ref="editUserDialogRef" @ok="getList"></edit-user-dialog>
  </div>
</template>

<script>
import {
  getUserListApi,
  deleteUserApi,
  sendCodeApi,
  userLoginApi,
  userReservationApi,
  travelRewardApi,
} from "@/api/imt/user";

import EditUserDialog from  './EditUserDialog.vue'

export default {
  name: "User",
  components: {
    EditUserDialog
  },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      userList: [],
      dateRange: [],
      title: "",
      open: false,
      openUser: false,
      refreshToken: false,
      state: false,
      stateNum: 60,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mobile: null,
        userId: null,
        token: null,
        itemCode: null,
        deviceId: null,
        provinceName: null,
        cityName: null,
        address: null,
        lat: null,
        lng: null,
        shopType: null,
        jsonResult: null,
        expireTime: null,
      },

      toAdd: 0,
      appointmentTypeList: [], // 预约类型
      appointmentTimeTypeList: [], // 预约时间类型
      // I茅台预约商品列表格数据
      itemList: [],
      //选择的数据
      itemSelect: [],

      form: {
        appointmentType: "",
        appointmentTimeType: "",
      },
      rules: {
        mobile: [
          {required: true, message: "手机号不能为空", trigger: "blur"},
        ],
      },
    };
  },
  created() {
    this.getList();

  },
  methods: {
    guid() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(
        /[xy]/g,
        function (c) {
          var r = (Math.random() * 16) | 0,
            v = c == "x" ? r : (r & 0x3) | 0x8;
          return v.toString(16);
        }
      );
    },
    getList() {
      this.loading = true;
      getUserListApi(this.queryParams).then(
        (res) => {
          this.userList = res.data.list;
          this.total = res.data.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openUser = false;
      this.refreshToken = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {

      };
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.dateRange = [];
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.mobile);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加I茅台用户";
      this.toAdd = 0;
      this.itemSelect = [];
      this.$refs.editUserDialogRef.show()
    },
    handleAddIUser() {
      this.reset();
      this.openUser = true
    },
    handleUpdate(row) {
      debugger
      console.log(122)
      this.$refs.editUserDialogRef.show(row)
    },
    //预约
    reservationFn(row) {
      const mobile = row.mobile || this.ids;
      userReservationApi({mobile: mobile}).then((response) => {
        this.$modal.msgSuccess("请求成功，结果看日志");
      });
    },
    //小茅运旅行活动
    travelReward(row) {
      const mobile = row.mobile || this.ids;
      travelRewardApi({mobile: mobile}).then((response) => {
        this.$modal.msgSuccess("请求成功，结果看日志");
      });
    },

    sendCodeFn(mobile, deviceId) {
      if (deviceId == undefined || deviceId == "") {
        this.form.deviceId = this.guid();
      } else {
        this.form.deviceId = deviceId;
      }
      let data = {
        mobile: mobile,
        deviceId: this.form.deviceId
      }
      sendCodeApi(data).then((response) => {
        this.$modal.msgSuccess("发送成功");
        this.state = true;
        let timer = setInterval(() => {
          this.stateNum--;
          if (this.stateNum === 0) {
            clearInterval(timer);
            this.state = false;
            this.stateNum = 60;
          }
        }, 1000);
      });
    },
    //登录
    loginFn(mobile, code) {
      this.refresh(mobile, code, this.form.deviceId, 0);
    },
    handleDelete(row) {
      const mobiles = row.mobile || this.ids;
      this.$modal
        .confirm('是否确认删除I茅台用户编号为"' + mobiles + '"的数据项？')
        .then(function () {
          return deleteUserApi({ mobile: mobiles });
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
    refresh(mobile, code, deviceId, status) {
      const msg = status ? "刷新成功" : "登录成功";
      let data = {
        mobile,
        code,
        deviceId
      }
      userLoginApi(data).then((response) => {
        this.$modal.msgSuccess(msg);
        this.open = false;
        this.openUser = false;
        this.refreshToken = false;
        this.getList();
      });
    },
    handleUpdateToken(row) {
      this.refreshToken = true;
      this.form = {
        mobile: row.mobile,
        deviceId: row.deviceId,
      };
      this.title = "刷新用户:" + row.name + "(" + row.mobile + ")登录信息";
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
}
</style>
