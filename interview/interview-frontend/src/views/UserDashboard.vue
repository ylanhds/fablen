<template>
  <div class="dashboard-container">
    <!-- 头部 -->
    <div class="header">
      <h2>仪表盘</h2>
      <div class="user-info">
        <p>欢迎！{{ username }}</p>
        <button @click="logout">登出</button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <input v-model="searchQuery" placeholder="根据名称搜索商品..." />
      <button @click="fetchProducts">搜索</button>
      <button @click="resetSearch">重置</button>
    </div>

    <!-- 商品统计 -->
    <div class="product-stats">
      共 {{ totalElements }} 条商品，当前第 {{ currentPage }} 页 / 共 {{ totalPages }} 页
    </div>

    <!-- 商品列表 -->
    <h3>商品列表</h3>
    <table class="product-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>产品名称</th>
          <th>价格</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td>{{ product.id }}</td>
          <td>{{ product.name }}</td>
          <td>{{ product.price.toFixed(2) }}</td>
          <td>
            <button @click="setEditProduct(product)">编辑</button>
            <button @click="deleteProduct(product.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 分页 -->
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
      <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
    </div>

    <!-- 添加商品 -->
    <div class="form-section add-product">
      <h3>添加商品</h3>
      <form @submit.prevent="addProduct">
        <input v-model="newProduct.name" placeholder="产品名称" required />
        <input v-model.number="newProduct.price" type="number" placeholder="价格" required />
        <button type="submit">添加</button>
      </form>
    </div>

    <!-- 编辑商品 -->
    <div class="form-section edit-product" v-if="editingProduct.id">
      <h3>编辑商品</h3>
      <form @submit.prevent="updateProduct">
        <input v-model="editingProduct.id" placeholder="ID" readonly />
        <input v-model="editingProduct.name" placeholder="产品名称" required />
        <input v-model.number="editingProduct.price" type="number" placeholder="价格" required />
        <button type="submit">更新</button>
        <button @click="cancelEdit" type="button">取消</button>
      </form>
    </div>
  </div>
</template>

<script>
import { apiClient } from '../apiClient';

export default {
  name: 'UserDashboard',
  data() {
    return {
      products: [],
      newProduct: { name: '', price: null },
      editingProduct: { id: null, name: '', price: null },
      searchQuery: '',
      currentPage: 1,
      itemsPerPage: 5,
      totalPagesFromBackend: 1,
      totalElementsFromBackend: 0
    };
  },
  computed: {
    username() {
      return localStorage.getItem('username') || '用户';
    },
    totalPages() {
      return this.totalPagesFromBackend;
    },
    totalElements() {
      return this.totalElementsFromBackend;
    }
  },
  created() {
    this.fetchProducts();
  },
  methods: {
    async fetchProducts() {
      try {
        const res = await apiClient.get('/product/api/products', {
          params: {
            page: this.currentPage - 1,
            size: this.itemsPerPage,
            name: this.searchQuery
          }
        });

        if (res.data.code === 200 && res.data.data) {
          this.products = res.data.data.content || [];
          this.totalPagesFromBackend = res.data.data.totalPages || 1;
          this.totalElementsFromBackend = res.data.data.totalElements || 0;
        } else {
          this.products = [];
          this.totalPagesFromBackend = 1;
          this.totalElementsFromBackend = 0;
        }

      } catch (error) {
        console.error('获取商品失败:', error);
        this.products = [];
        this.totalPagesFromBackend = 1;
        this.totalElementsFromBackend = 0;
      }
    },
    resetSearch() {
      this.searchQuery = '';
      this.fetchProducts();
    },
    async addProduct() {
      try {
        const res = await apiClient.post('/product/api/products/add', this.newProduct);
        this.products.push(res.data.data);
        this.newProduct = { name: '', price: null };
      } catch (error) {
        console.error('添加商品失败:', error);
      }
    },
    setEditProduct(product) {
      this.editingProduct = { ...product };
    },
    async updateProduct() {
      try {
        const res = await apiClient.put(`/product/api/products/edit/${this.editingProduct.id}`, this.editingProduct);
        const index = this.products.findIndex(p => p.id === this.editingProduct.id);
        if (index !== -1) {
          this.products.splice(index, 1, res.data);
        }
        this.cancelEdit();
      } catch (error) {
        console.error('更新商品失败:', error);
      }
    },
    cancelEdit() {
      this.editingProduct = { id: null, name: '', price: null };
    },
    async deleteProduct(id) {
      try {
        await apiClient.delete(`/product/api/products/delete/${id}`);
        this.products = this.products.filter(p => p.id !== id);
      } catch (error) {
        console.error('删除商品失败:', error);
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchProducts();
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPagesFromBackend) {
        this.currentPage++;
        this.fetchProducts();
      }
    },
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.dashboard-container {
  max-width: 1000px;
  margin: 40px auto;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 30px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.user-info {
  text-align: right;
}

.user-info p {
  margin: 0;
  font-weight: bold;
}

.search-bar {
  margin-bottom: 20px;
}

.search-bar input {
  width: calc(100% - 160px);
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
}

.search-bar button {
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
}

.product-stats {
  margin-bottom: 10px;
  font-weight: bold;
  color: #555;
}

.product-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.product-table th,
.product-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

.product-table th {
  background-color: #f7f7f7;
  color: #333;
}

.product-table tr:nth-child(even) {
  background-color: #fafafa;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.pagination button {
  padding: 8px 12px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  border-radius: 4px;
}

.pagination button:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}

.form-section {
  margin-top: 30px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 6px;
}

.form-section h3 {
  margin-bottom: 15px;
}

.form-section input {
  display: block;
  margin: 8px 0;
  padding: 10px;
  width: 300px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-section button {
  margin-right: 10px;
  padding: 8px 16px;
  border: none;
  background-color: #28a745;
  color: white;
  cursor: pointer;
  border-radius: 4px;
}
</style>
