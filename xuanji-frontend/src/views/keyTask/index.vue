<template>
  <div class="key-task-list">
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="主要领域">
          <el-input v-model="queryParams.mainDomain" placeholder="请输入主要领域" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="牵头部门">
          <el-select v-model="queryParams.leadDepartment" placeholder="请选择牵头部门" clearable>
            <el-option
              v-for="dept in departments"
              :key="dept"
              :label="dept"
              :value="dept">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="牵头人">
          <el-select v-model="queryParams.leadPerson" placeholder="请选择牵头人" clearable>
            <el-option
              v-for="person in persons"
              :key="person"
              :label="person"
              :value="person">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="success" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table
      v-loading="loading"
      :data="keyTaskList"
      style="width: 100%">
      <el-table-column
        prop="mainDomain"
        label="主要领域"
        width="150">
      </el-table-column>
      <el-table-column
        prop="taskName"
        label="重点任务名称"
        min-width="200">
      </el-table-column>
      <el-table-column
        label="关键词"
        width="200">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.keyword1" size="mini" type="primary">{{ scope.row.keyword1 }}</el-tag>
          <el-tag v-if="scope.row.keyword2" size="mini" type="success" style="margin-left: 5px;">{{ scope.row.keyword2 }}</el-tag>
          <el-tag v-if="scope.row.keyword3" size="mini" type="warning" style="margin-left: 5px;">{{ scope.row.keyword3 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="leadDepartment"
        label="牵头部门"
        width="150">
      </el-table-column>
      <el-table-column
        prop="leadPerson"
        label="牵头人"
        width="120">
      </el-table-column>
      <el-table-column
        label="计划时间"
        width="180">
        <template slot-scope="scope">
          {{ scope.row.plannedStartDate }} 至 {{ scope.row.plannedEndDate }}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="180"
        fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleDetail(scope.row)">详情</el-button>
          <el-button
            size="mini"
            type="primary"
            @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryParams.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <!-- 详情对话框 -->
    <el-dialog title="重点任务详情" :visible.sync="detailVisible" width="80%">
      <key-task-detail v-if="detailVisible" :task-id="selectedTaskId" />
    </el-dialog>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="formVisible" width="60%">
      <key-task-form 
        v-if="formVisible" 
        :task-id="selectedTaskId" 
        @success="handleFormSuccess"
        @cancel="formVisible = false" />
    </el-dialog>
  </div>
</template>

<script>
import { getKeyTaskPage, deleteKeyTask, getAllLeadDepartments, getAllLeadPersons } from '@/api/keyTask'
import KeyTaskDetail from './components/KeyTaskDetail'
import KeyTaskForm from './components/KeyTaskForm'

export default {
  name: 'KeyTaskList',
  components: {
    KeyTaskDetail,
    KeyTaskForm
  },
  data() {
    return {
      loading: false,
      keyTaskList: [],
      total: 0,
      queryParams: {
        current: 1,
        size: 10,
        mainDomain: '',
        leadDepartment: '',
        leadPerson: ''
      },
      departments: [],
      persons: [],
      detailVisible: false,
      formVisible: false,
      selectedTaskId: null,
      dialogTitle: ''
    }
  },
  created() {
    this.getList()
    this.getDepartments()
    this.getPersons()
  },
  methods: {
    getList() {
      this.loading = true
      getKeyTaskPage(this.queryParams).then(response => {
        this.keyTaskList = response.data.records
        this.total = response.data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getDepartments() {
      getAllLeadDepartments().then(response => {
        this.departments = response.data
      })
    },
    getPersons() {
      getAllLeadPersons().then(response => {
        this.persons = response.data
      })
    },
    handleQuery() {
      this.queryParams.current = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        current: 1,
        size: 10,
        mainDomain: '',
        leadDepartment: '',
        leadPerson: ''
      }
      this.getList()
    },
    handleSizeChange(val) {
      this.queryParams.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.current = val
      this.getList()
    },
    handleAdd() {
      this.selectedTaskId = null
      this.dialogTitle = '新增重点任务'
      this.formVisible = true
    },
    handleEdit(row) {
      this.selectedTaskId = row.id
      this.dialogTitle = '编辑重点任务'
      this.formVisible = true
    },
    handleDetail(row) {
      this.selectedTaskId = row.id
      this.detailVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该重点任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteKeyTask(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      })
    },
    handleFormSuccess() {
      this.formVisible = false
      this.getList()
    }
  }
}
</script>

<style scoped>
.key-task-list {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>