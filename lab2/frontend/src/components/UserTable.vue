<template>
  <div>
    <Toast/>

    <div class="card">
      <h3>User Management:</h3>
      <DataTable :value="users" v-model:selection="selectedUser" selectionMode="single" dataKey="id"
                 @rowSelect="onRowSelect" @rowUnselect="onRowUnselect" :metaKeySelection="false" responsiveLayout="scroll">
        <Column field="id" header="ID"></Column>
        <Column field="user_name" header="Name"></Column>
        <Column field="user_email" header="E-Mail"></Column>
      </DataTable>
    </div>

    <div class="card">
      <div class="grid p-fluid">
        <div class="field col-12 md:col-4">
          <div class="p-inputgroup">
                    <span class="p-inputgroup-addon">
                        <i class="pi pi-user"></i>
                    </span>
            <InputText v-model="user_name" placeholder="Username"/>
          </div>
        </div>
        <div class="field col-12 md:col-4">
          <div class="p-inputgroup">
                    <span class="p-inputgroup-addon">
                        <i class="pi pi-envelope"></i>
                    </span>
            <InputText v-model="user_email" placeholder="E-Mail"/>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="p-fluid formgrid grid">
        <div class="col-12 md:col-4">
          <Button @click="onButtonCreate" label="Create" icon="pi pi-plus" iconPos="left"/>
        </div>
        <div class="col-12 md:col-4">
          <Button @click="onButtonUpdate" label="Update" icon="pi pi-refresh" class="p-button-secondary" iconPos="left"/>
        </div>
        <div class="col-12 md:col-4">
          <Button @click="onButtonDelete" label="Delete" icon="pi pi-minus" class="p-button-danger" iconPos="left"/>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import ProductService from './services/ProductService';
import UserService from "@/components/services/UserService";

export default {
  name: "UserTable",
  data() {
    return {
      users: null,
      selectedUser: null,
      user_id: '',
      user_name: '',
      user_email: ''
    }
  },
  userService: null,
  created() {
    this.userService = new UserService();
  },
  mounted() {
    this.reloadData();
  },
  methods: {
    onRowSelect(event) {
      this.user_id = this.selectedUser.id;
      this.user_name = this.selectedUser.user_name;
      this.user_email = this.selectedUser.user_email;
      this.$toast.add({severity: 'info', summary: 'Product Selected', detail: 'Name: ' + event.data.name, life: 3000});
    },
    onRowUnselect(event) {
      this.$toast.add({
        severity: 'warn',
        summary: 'Product Unselected',
        detail: 'Name: ' + event.data.name,
        life: 3000
      });
    },
    onButtonCreate() {
      this.userService.createUser(this.user_name, this.user_email)
          .finally(() => this.reloadData());
    },
    onButtonUpdate() {
      this.userService.updateUser(this.user_id, this.user_name, this.user_email)
          .finally(() => this.reloadData());
    },
    onButtonDelete() {
      this.userService.deleteUser(this.user_id)
        .finally(() => this.reloadData());
    },
    reloadData() {
      this.userService.getUsers().then(data => this.users = data);
    }
  }
}
</script>

<style scoped>
h3 {
  font-size: 1.2rem;
  top: -15px;
}
</style>