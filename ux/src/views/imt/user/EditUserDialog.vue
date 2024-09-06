<template>
  <!-- 添加或修改I茅台用户对话框 -->
  <el-dialog :title="title" :visible.sync="visible" width="650px" append-to-body>
    <el-form ref="form" :model="form" :rules="rules" size="mini" label-width="110px">
      <el-form-item v-if="toAdd != 1" label="手机号" prop="mobile">
        <el-input v-model="form.mobile" placeholder="请输入I茅台用户手机号"/>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" placeholder="请输入备注"/>
      </el-form-item>
      <el-form-item label="用户id" prop="id">
        <el-input v-model="form.id" placeholder="请输入I茅台用户id"/>
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
        <el-select v-model="form.itemCode" multiple placeholder="请选择" >
          <el-option v-for="item in itemList" :key="item.itemCode" :label="item.title" :value="item.itemCode"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="appointmentType">
        <el-select v-model="form.appointmentType" placeholder="请选择">
          <el-option v-for="item in appointmentTypeList" :key="item.code" :label="item.name" :value="item.code"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="省" prop="provinceName">
        <el-select v-model="form.provinceName" placeholder="请选择省" filterable @change="getCityList">
          <el-option v-for="item in provinceList" :key="item.provinceName" :label="item.provinceName" :value="item.provinceName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="市" prop="cityName">
        <el-select v-model="form.cityName" placeholder="请选择市" filterable  @change="getDistrictList">
          <el-option v-for="item in cityList" :key="item.cityName" :label="item.cityName" :value="item.cityName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="区" prop="districtName">
        <el-select v-model="form.districtName" placeholder="请选择市" filterable  @change="getShopList">
          <el-option label="不限" value=""></el-option>
          <el-option v-for="item in districtList" :key="item.districtName" :label="item.districtName" :value="item.districtName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="门店ID" prop="shopId" v-if="form.appointmentType === 2">
        <el-select v-model="form.shopId" placeholder="请选择门店" filterable  @change="getShopList">
          <el-option v-for="item in shopList" :key="item.shopId" :label="item.name" :value="item.shopId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="随机时间预约" prop="appointmentTimeType">
        <el-select v-model="form.appointmentTimeType" placeholder="请选择">
          <el-option v-for="item in appointmentTimeTypeList" :key="item.code" :label="item.name" :value="item.code"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分钟" prop="minute" v-if="form.appointmentTimeType === 2">
        <el-input v-model="form.minute" placeholder="预约执行的时间(单位分)，例如15分执行"/>
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
import { getProvinceListApi, getCityListApi, getDistrictListApi, getListShopAllApi } from "@/api/imt/shop";
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
      itemList: [],
      provinceList: [],
      cityList: [],
      shopList: [],
      districtList: [],
      form: {
        mobile: '',
        remark: '',
        id: '',
        token: '',
        cookie: '',
        deviceId: '',
        itemCode: [],
        appointmentType: "",
        appointmentTimeType: "",
        provinceName: "",
        cityName: "",
        districtName: "",
        shopId: "",
        minute: "",
        expireTime: "",
      },
      rules: {
        mobile: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        id: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        remark: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        token: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        cookie: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        deviceId: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        itemCode: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        appointmentType: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        appointmentTimeType: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        provinceName: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        cityName: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        districtName: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        shopId: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        minute: [{required: true, message: "手机号不能为空", trigger: "blur"}],
        expireTime: [{required: true, message: "手机号不能为空", trigger: "blur"}],
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
    getProvinceListApi().then(res => {
      this.provinceList = res.data
    })
  },
  methods: {
    getCityList() {
      getCityListApi({provinceName: this.form.provinceName}).then(res => {
        this.cityList = res.data
      })
    },
    getDistrictList() {
      this.getShopList()
      getDistrictListApi({provinceName: this.form.provinceName, cityName: this.form.cityName}).then(res => {
        this.districtList = res.data
      })
    },
    getShopList() {
      getListShopAllApi({provinceName: this.form.provinceName, cityName: this.form.cityName, districtName: this.form.districtName}).then(res => {
        this.shopList = res.data.list
      })
    },
    // 业务类型 订单类型
    show(row) {
      debugger
      console.log(12222)
      if(!row) {
        this.title = "新增I茅台用户";
        this.visible = true;
      } else {
        let data = {
          mobile: row.mobile
        }
        getUserByMobileApi(data).then((response) => {
          this.toAdd = 1;
          this.form = response.data;
          this.visible = true;
          this.title = "修改I茅台用户";
          let itemCode = [];
          if (response.data.itemCode !== "" && response.data.itemCode.indexOf("@") == -1) {
           itemCode.push(response.data.itemCode);
          } else {
            let arr = response.data.itemCode.split("@");
            arr.forEach((e) => {
              if (e !== "") {
                itemCode.push(e);
              }
            });
          }
          this.form.itemCode = [];
        });
      }
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let data = {
            ...this.form
          }
          let itemCode = ""
          this.form.itemCode.forEach((e) => {
            itemCode += e + "@";
          });
          data.itemCode = itemCode
          if (this.toAdd != 0) {
            updateUserApi(data).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            userLoginApi(data).then((response) => {
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
