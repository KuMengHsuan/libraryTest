<template>
  <div id="app">
    <h1>圖書館</h1>
    <!-- 渲染 route 的位置 -->
    <router-view></router-view>

    <DataTable :value="bookList" tableStyle="min-width: 50rem">
      <Column field="isbn" header="ISBN"></Column>
      <Column field="name" header="書名"></Column>
      <Column field="author" header="作者"></Column>
      <Column field="status" header="狀態">
        <template #body="slotProps">
          {{ getBookStatus(slotProps.data.status) }}
        </template>
      </Column>
      <Column header="借閱">
        <template #body="slotProps">
          <Button
            label="借閱"
            v-on:click="borrow(slotProps.data)"
            :disabled="slotProps.data.status !== 'N'"
          />
        </template>
      </Column>
    </DataTable>
    <!-- 登入按鈕 -->
  </div>
</template>
<script>
import axios from "axios";
import router from "../../router";
export default {
  data() {
    return {
      bookList: [],
      userId: "",
    };
  },
  created() {
    this.refresh();
  },
  methods: {
    async refresh() {
      const userId = sessionStorage.getItem("user_id");
      if (userId && userId.trim !== "") {
        this.userId = userId;
      }
      await axios
        .post("http://localhost:8085/book/query", {})
        .then((response) => {
          console.log(response.data);
          this.bookList = [...response.data];
        })
        .catch((error) => {
          console.log("查詢失敗", error);
          alert("查詢失敗");
        });
    },
    getBookStatus(value) {
      // const str = "";
      switch (value) {
        case "N":
          return "可借閱";
        case "Y":
          return "已被借閱";
        // break;
        default:
          return "";
      }
    },
    async borrow(data) {
      this.userId = sessionStorage.getItem("user_id");
      if (!this.userId || this.userId.trim === "") {
        alert("請先登入");
        router.push("/login");
        return;
      }
      if (data.status === "N") {
        const queryData = {
          inventory_id: data.inventory_id,
          user_id: this.userId,
        };
        await axios
          .post("http://localhost:8085/book/borrow", queryData)
          .then((response) => {
            console.log(response.data);
            alert("借閱成功");
            this.refresh();
          })
          .catch((error) => {
            console.log("借閱失敗", error);
            alert("借閱失敗");
            this.refresh();
          });
      } else {
        alert("此書不可進行借閱");
      }
    },
    signOut() {
      sessionStorage.clear();
      this.userId = "";
    },
  },
};
</script>