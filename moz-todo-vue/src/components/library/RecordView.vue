<template>
  <DataTable :value="recordList" tableStyle="min-width: 50rem">
    <Column field="isbn" header="ISBN"></Column>
    <Column field="name" header="書名"></Column>
    <Column field="author" header="作者"></Column>
    <Column field="borrowing_time" header="借書日期">
      <!-- <template #body="slotProps">
        {{ getBookStatus(slotProps.data.status) }}
      </template> -->
    </Column>
    <Column field="return_time" header="還書日期">
      <!-- <template #body="slotProps">
        {{ slotProps.data.status| timeString  }}
      </template> -->
    </Column>
    <Column header="還書">
      <template #body="slotProps">
        <Button
          label="還書"
          v-on:click="borrowReturn(slotProps.data)"
          :disabled="slotProps.data.return_time !== null"
        />
      </template>
    </Column>
  </DataTable>
</template>

<script>
import axios from "axios";
import router from "../../router";
export default {
  data() {
    return {
      recordList: [],
      userId: "",
    };
  },
  created() {
    this.refresh();
  },
  methods: {
    async refresh() {
      this.userId = sessionStorage.getItem("user_id");
      console.log(this.userId);
      if (!this.userId || this.userId.trim === "") {
        alert("請先登入");
        router.push("/login");
        return;
      }
      const data = {
        user_id: this.userId,
      };
      await axios
        .post("http://localhost:8085/record/query", data)
        .then((response) => {
          console.log(response.data);
          this.recordList = [...response.data];
        })
        .catch((error) => {
          console.log("查詢失敗", error);
          alert("查詢失敗");
        });
    },
    setDate(date) {
      console.log(typeof date);
      //   if (typeof date==='Object') {

      //   }
    },
    async borrowReturn(data) {
      this.userId = sessionStorage.getItem("user_id");
      console.log(this.userId);
      if (!this.userId || this.userId.trim === "") {
        alert("請先登入");
        router.push("/login");
        return;
      }
      if (data.return_time === null) {
        const queryData = {
          inventory_id: data.inventory_id,
          user_id: this.userId,
        };
        await axios
          .post("http://localhost:8085/book/return", queryData)
          .then((response) => {
            console.log(response.data);
            alert("還書成功");
            this.refresh();
          })
          .catch((error) => {
            console.log("還書失敗", error);
            alert("還書失敗");
            this.refresh();
          });
      } else {
        alert("此書不可進行還書");
      }
    },
  },
};
</script>