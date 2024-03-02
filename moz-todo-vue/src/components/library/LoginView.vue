<template>
  <div class="container">
    <h2>登入</h2>
    <!-- 姓名 -->
    <div class="form-group" v-if="isRegisterView">
      <label for="username">使用者名稱</label>
      <InputText type="text" v-model="username" />
    </div>
    <!-- 電話 -->
    <div class="form-group">
      <label for="phoneNumber">手機號碼</label>
      <InputText type="text" v-model="phoneNumber" />
    </div>
    <!-- 密碼 -->
    <div class="form-group">
      <label for="password">密碼</label>
      <InputText type="password" v-model="password" />
    </div>
    <!-- 確認密碼 -->
    <div class="form-group" v-if="isRegisterView">
      <label for="confirmPassword">確認密碼</label>
      <InputText type="password" v-model="confirmPassword" />
    </div>
    <!-- 登入按鈕 -->
    <Button label="登入" v-on:click="login" v-if="!isRegisterView" />
    <!-- 切換成註冊 -->
    <Button
      link
      label="進行註冊"
      v-on:click="showRegister"
      v-if="!isRegisterView"
    />
    <!-- 註冊按鈕 -->
    <Button label="註冊" v-on:click="register" v-if="isRegisterView" />
  </div>
</template>
<script>
import axios from "axios";
import router from '../../router'
export default {
  data() {
    return {
      username: "",
      password: "",
      phoneNumber: "",
      confirmPassword: "",
      isRegisterView: false,
    };
  },
  methods: {
    showRegister() {
      this.isRegisterView = true;
    },
    async register() {
      if (this.confirmPassword !== this.password) {
        alert("請確認密碼是否一致");
        return;
      }
      if (
        this.password.trim() === "" ||
        this.phoneNumber.trim() === "" ||
        this.username.trim() === ""
      ) {
        alert("請輸入註冊資訊");
        return;
      }
      const data = {
        password: this.password,
        user_name: this.username,
        phone_number: this.phoneNumber,
      };
      await axios
        .post("http://localhost:8085/user/register", data)
        .then((response) => {
          console.log("User created:", response.data);
          if (response.data.status === "Y") {
            alert("註冊成功");
            this.isRegisterView = false;
            this.setDefualt();
          }
        })
        .catch((error) => {
          console.log("註冊失敗", error);
          alert("註冊失敗");
        });
    },
    setDefualt() {
      this.password = "";
      this.username = "";
      this.phoneNumber = "";
    },
    async login() {
      if (this.password.trim() === "" || this.phoneNumber.trim() === "") {
        alert("請輸入登入資訊");
        return;
      }
      const data = {
        password: this.password,
        phone_number: this.phoneNumber,
      };
      await axios
        .post("http://localhost:8085/user/login", data)
        .then((response) => {
          console.log(response.data);
          if (response.data.status === "Y") {
            alert("登入成功");
            this.setDefualt();
            sessionStorage.setItem("user_id", response.data.user_id);
            router.push('/');
          }
        })
        .catch((error) => {
          console.log("登入失敗", error);
          alert("登入失敗");
          // 清空表单
          this.password = "";
        });
    },
  },
};
</script>