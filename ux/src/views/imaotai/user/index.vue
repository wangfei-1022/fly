<template>
  <div class="app-container">
    <div class="search-wrap">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="queryParams.mobile" placeholder="请输入I茅台手机号" clearable @keyup.enter.native="handleQuery"/>
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
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="手机号" align="center" prop="mobile" min-width="120"/>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="预约项目code" align="center" prop="itemCode" min-width="120"/>
      <el-table-column label="省份" align="center" prop="provinceName"/>
      <el-table-column label="类型" align="center" prop="shopType" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.shopType == 1 ? "预约出货量最大门店" : "预约附近门店"}}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约执行分钟" align="center" prop="minute" min-width="100"/>

      <el-table-column label="到期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, "{y}-{m}-{d} {h}:{i}:{s}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="280">
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

    <!-- 添加或修改I茅台用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" size="mini" label-width="100px">
        <el-form-item v-if="toAdd != 1" label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入I茅台用户手机号"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户id" prop="userId">
              <el-input v-model="form.userId" placeholder="请输入I茅台用户id"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="toekn" prop="token">
          <el-input v-model="form.token" placeholder="请输入I茅台toekn"/>
        </el-form-item>
        <el-form-item label="cookie" prop="cookie">
          <el-input v-model="form.cookie" placeholder="请输入I茅台cookie"/>
        </el-form-item>
        <el-form-item label="设备id" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备id"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预约code" prop="itemCode">
              <el-select v-model="itemSelect" multiple placeholder="请选择"  @change="changeItem">
                <el-option v-for="item in itemList" :key="item.itemCode" :label="item.title" :value="item.itemCode"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分钟" prop="minute">
              <el-input v-model="form.minute" placeholder="预约执行的时间(单位分)，例如15分执行"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="随机时间预约" prop="randomMinute">
              <el-select v-model="form.randomMinute" placeholder="随机时间预约">
                <el-option v-for="item in itemList" :key="item.itemCode" :label="item.title" :value="item.itemCode"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="shopType">
              <el-select v-model="form.shopType" placeholder="请选择">
                <el-option v-for="item in typeOptions" :key="item.code" :label="item.name" :value="item.code"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="门店商品ID" prop="ishopId">
              <el-input v-model="form.ishopId" placeholder="请输入门店商品ID"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="推送token" prop="pushPlusToken">
              <el-input v-model="form.pushPlusToken" placeholder="请输入推送token"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="到期时间" prop="expireTime">
          <el-date-picker v-model="form.expireTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="submitForm">确 定</el-button>
        <el-button size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

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
          <el-input v-model="form.mobile" readonly placeholder="请输入I茅台用户手机号"/>
          <div style="margin-top: 10px">
            <el-button type="primary" @click="sendCode(form.mobile, form.deviceId)" :disabled="state">发送验证码<span v-if="state">({{ stateNum }})</span></el-button>
          </div>
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
  </div>
</template>

<script>
import {
  getUserListApi,
  getUserByMobileApi,
  delUser,
  addUser,
  updateUserApi,
  sendCodeApi,
  userLoginApi,
  userReservationApi,
  travelReward,
} from "@/api/imaotai/user";
import { getItemListApi } from "@/api/imaotai/item";
import { getAppointmentTypeApi } from "@/api/imaotai/base";

export default {
  name: "User",
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
      form: {},
      rules: {
        mobile: [
          {required: true, message: "手机号不能为空", trigger: "blur"},
        ],
      },
      toAdd: 0,
      typeOptions: [],
      // I茅台预约商品列表格数据
      itemList: [],
      //选择的数据
      itemSelect: [],
    };
  },
  created() {
    this.getList();
    getItemListApi().then(res => {
      this.itemList = res.data.list;
    });
    getAppointmentTypeApi().then(res => {
      this.typeOptions = res.data;
    })
  },
  methods: {
    //item下拉框选择
    changeItem(e) {
      this.form.itemCode = "";
      this.itemSelect.forEach((e) => {
        this.form.itemCode += e + "@";
      });
    },
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
    },
    handleAddIUser() {
      this.reset();
      this.openUser = true;
    },
    handleUpdate(row) {
      this.reset();
      const mobile = row.mobile || this.ids[0];
      let data = {
        mobile: mobile
      }
      getUserByMobileApi(data).then((response) => {
        this.toAdd = 1;
        this.form = response.data;
        this.open = true;
        this.title = "修改I茅台用户";
        this.itemSelect = [];
        if(this.form.itemCode) {
          if (this.form.itemCode !== "" && this.form.itemCode.indexOf("@") == -1) {
            this.itemSelect.push(this.form.itemCode);
          } else {
            let arr = this.form.itemCode.split("@");
            arr.forEach((e) => {
              if (e !== "") {
                this.itemSelect.push(e);
              }
            });
          }
        }
      });
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
      travelReward(mobile).then((response) => {
        this.$modal.msgSuccess("请求成功，结果看日志");
      });
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.toAdd != 0) {
            updateUserApi(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            userLoginApi(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
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
          return delUser(mobiles);
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
      this.title = "刷新用户:" + row.remark + "(" + row.mobile + ")登录信息";
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
