<template>
  <!-- 添加或修改I茅台用户对话框 -->
  <el-dialog :title="title" :visible.sync="visible" width="600px" append-to-body>
    <el-form ref="form" :model="form" :rules="rules" size="mini" label-width="100px">
      <el-form-item v-if="toAdd != 1" label="手机号" prop="mobile">
        <el-input v-model="form.mobile" placeholder="请输入I茅台用户手机号"/>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" placeholder="请输入备注"/>
      </el-form-item>
      <el-form-item label="用户id" prop="userId">
        <el-input v-model="form.userId" placeholder="请输入I茅台用户id"/>
      </el-form-item>
      <el-form-item label="toekn" prop="token" class="full-line">
        <el-input v-model="form.token" placeholder="请输入I茅台toekn"/>
      </el-form-item>
      <el-form-item label="cookie" prop="cookie" class="full-line">
        <el-input v-model="form.cookie" placeholder="请输入I茅台cookie"/>
      </el-form-item>
      <el-form-item label="设备id" prop="deviceId" class="full-line">
        <el-input v-model="form.deviceId" placeholder="请输入设备id"/>
      </el-form-item>
      <el-form-item label="预约code" prop="itemCode">
        <el-select v-model="itemSelect" multiple placeholder="请选择"  @change="changeItem">
          <el-option v-for="item in itemList" :key="item.itemCode" :label="item.title" :value="item.itemCode"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="appointmentType">
        <el-select v-model="form.appointmentType" placeholder="请选择">
          <el-option v-for="item in appointmentTypeList" :key="item.code" :label="item.name" :value="item.code"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="省" prop="provinceName">
        <el-input v-model="form.provinceName" placeholder="请输入门店省市"/>
      </el-form-item>
      <el-form-item label="市" prop="cityName">
        <el-input v-model="form.cityName" placeholder="请输入门店省市"/>
      </el-form-item>
      <el-form-item label="门店商品ID" prop="ishopId" v-if="form.appointmentType === 2">
        <el-input v-model="form.ishopId" placeholder="请输入门店商品ID"/>
      </el-form-item>
      <el-form-item label="随机时间预约" prop="appointmentTimeType">
        <el-select v-model="form.appointmentTimeType" placeholder="请选择">
          <el-option v-for="item in appointmentTimeTypeList" :key="item.code" :label="item.name" :value="item.code"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分钟" prop="minute" v-if="form.appointmentTimeType === 2">
        <el-input v-model="form.minute" placeholder="预约执行的时间(单位分)，例如15分执行"/>
      </el-form-item>
      <el-form-item label="推送token" prop="pushPlusToken">
        <el-input v-model="form.pushPlusToken" placeholder="请输入推送token"/>
      </el-form-item>
      <el-form-item label="到期时间" prop="expireTime">
        <el-date-picker v-model="form.expireTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间" style="width: 180px;"></el-date-picker>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="mini" type="primary" @click="submitForm">确 定</el-button>
      <el-button size="mini" @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getAppointmentTypeApi, getAppointmentTimeTypeApi } from "@/api/imt/base";
import { getItemListApi } from "@/api/imt/item";
import {
  getUserByMobileApi,
  addUser,
  updateUserApi,
  userLoginApi } from "@/api/imt/user";
export default {
  name: "ApplyPayableClearDialog",
  props: {
    origin: {
      type: String,
      default: ''
    },
    toPath: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      title: '',
      toAdd: 0,
      appointmentTypeList: [],
      appointmentTimeTypeList: [],
      showLocalServicePaymentType: false,
      visible: false,
      itemSelect: [],
      itemList: [],
      form: {
        appointmentType: "",
        appointmentTimeType: "",
      },
      rules: {
        mobile: [
          {required: true, message: "手机号不能为空", trigger: "blur"},
        ],
      },
    }
  },
  created() {
    getItemListApi().then(res => {
      this.itemList = res.data.list;
    });
    getAppointmentTypeApi().then(res => {
      this.appointmentTypeList = res.data;
    })
    getAppointmentTimeTypeApi().then(res => {
      this.appointmentTimeTypeList = res.data;
    })
  },
  methods: {
    // 业务类型 订单类型
    show(row) {
      const mobile = row.mobile || this.ids[0];
      let data = {
        mobile: mobile
      }
      getUserByMobileApi(data).then((response) => {
        this.toAdd = 1;
        this.form = response.data;
        this.visible = true;
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
    //item下拉框选择
    changeItem(e) {
      this.form.itemCode = "";
      this.itemSelect.forEach((e) => {
        this.form.itemCode += e + "@";
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
  }
}
</script>
<style lang="scss" scoped>
.el-form {
  overflow: hidden;
}
.el-form-item--mini.el-form-item {
  width: 50%;
  float: left;
}

.el-form-item--mini.el-form-item.full-line {
  width: 100%;
}
</style>
