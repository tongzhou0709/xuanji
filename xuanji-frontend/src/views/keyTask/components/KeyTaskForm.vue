<template>
  <div class="key-task-form">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="主要领域" prop="mainDomain">
        <el-input v-model="form.mainDomain" placeholder="请输入主要领域"></el-input>
      </el-form-item>
      
      <el-form-item label="重点任务名称" prop="taskName">
        <el-input v-model="form.taskName" placeholder="请输入重点任务名称"></el-input>
      </el-form-item>
      
      <el-form-item label="具体举措" prop="specificMeasures">
        <el-input 
          v-model="form.specificMeasures" 
          type="textarea" 
          :rows="4"
          placeholder="请输入具体举措"></el-input>
      </el-form-item>
      
      <el-form-item label="预期实现效果" prop="expectedEffect">
        <el-input 
          v-model="form.expectedEffect" 
          type="textarea" 
          :rows="4"
          placeholder="请输入预期实现效果"></el-input>
      </el-form-item>
      
      <el-form-item label="关键词1">
        <el-input v-model="form.keyword1" placeholder="请输入关键词1"></el-input>
      </el-form-item>
      
      <el-form-item label="关键词2">
        <el-input v-model="form.keyword2" placeholder="请输入关键词2"></el-input>
      </el-form-item>
      
      <el-form-item label="关键词3">
        <el-input v-model="form.keyword3" placeholder="请输入关键词3"></el-input>
      </el-form-item>
      
      <el-form-item label="标志成果或交付物" prop="deliverables">
        <el-input 
          v-model="form.deliverables" 
          type="textarea" 
          :rows="4"
          placeholder="请输入标志成果或交付物"></el-input>
      </el-form-item>
      
      <el-form-item label="牵头部门" prop="leadDepartment">
        <el-select v-model="form.leadDepartment" placeholder="请选择牵头部门" style="width: 100%">
          <el-option
            v-for="dept in departments"
            :key="dept"
            :label="dept"
            :value="dept">
          </el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="牵头人" prop="leadPerson">
        <el-select v-model="form.leadPerson" placeholder="请选择牵头人" style="width: 100%">
          <el-option
            v-for="person in persons"
            :key="person"
            :label="person"
            :value="person">
          </el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="计划开始时间" prop="plannedStartDate">
        <el-date-picker
          v-model="form.plannedStartDate"
          type="date"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd"
          style="width: 100%">
        </el-date-picker>
      </el-form-item>
      
      <el-form-item label="计划完成时间" prop="plannedEndDate">
        <el-date-picker
          v-model="form.plannedEndDate"
          type="date"
          placeholder="选择完成时间"
          value-format="yyyy-MM-dd"
          style="width: 100%">
        </el-date-picker>
      </el-form-item>
    </el-form>
    
    <div class="form-actions">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </div>
  </div>
</template>

<script>
import { getKeyTaskById, createKeyTask, updateKeyTask, getAllLeadDepartments, getAllLeadPersons } from '@/api/keyTask'

export default {
  name: 'KeyTaskForm',
  props: {
    taskId: {
      type: Number,
      default: null
    }
  },
  data() {
    // 自定义验证规则：结束时间必须大于开始时间
    const validateEndDate = (rule, value, callback) => {
      if (value && this.form.plannedStartDate && value < this.form.plannedStartDate) {
        callback(new Error('完成时间必须晚于开始时间'))
      } else {
        callback()
      }
    }
    
    return {
      submitting: false,
      form: {
        mainDomain: '',
        taskName: '',
        specificMeasures: '',
        expectedEffect: '',
        keyword1: '',
        keyword2: '',
        keyword3: '',
        deliverables: '',
        leadDepartment: '',
        leadPerson: '',
        plannedStartDate: '',
        plannedEndDate: ''
      },
      rules: {
        mainDomain: [
          { required: true, message: '请输入主要领域', trigger: 'blur' }
        ],
        taskName: [
          { required: true, message: '请输入重点任务名称', trigger: 'blur' }
        ],
        specificMeasures: [
          { required: true, message: '请输入具体举措', trigger: 'blur' }
        ],
        expectedEffect: [
          { required: true, message: '请输入预期实现效果', trigger: 'blur' }
        ],
        deliverables: [
          { required: true, message: '请输入标志成果或交付物', trigger: 'blur' }
        ],
        leadDepartment: [
          { required: true, message: '请选择牵头部门', trigger: 'change' }
        ],
        leadPerson: [
          { required: true, message: '请选择牵头人', trigger: 'change' }
        ],
        plannedStartDate: [
          { required: true, message: '请选择计划开始时间', trigger: 'change' }
        ],
        plannedEndDate: [
          { required: true, message: '请选择计划完成时间', trigger: 'change' },
          { validator: validateEndDate, trigger: 'change' }
        ]
      },
      departments: [],
      persons: []
    }
  },
  created() {
    this.getDepartments()
    this.getPersons()
    if (this.taskId) {
      this.getTaskDetail()
    }
  },
  methods: {
    getTaskDetail() {
      getKeyTaskById(this.taskId).then(response => {
        this.form = response.data
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
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitting = true
          if (this.taskId) {
            // 编辑
            updateKeyTask({ ...this.form, id: this.taskId }).then(() => {
              this.$message.success('更新成功')
              this.$emit('success')
            }).finally(() => {
              this.submitting = false
            })
          } else {
            // 新增
            createKeyTask(this.form).then(() => {
              this.$message.success('创建成功')
              this.$emit('success')
            }).finally(() => {
              this.submitting = false
            })
          }
        }
      })
    },
    handleCancel() {
      this.$emit('cancel')
    }
  }
}
</script>

<style scoped>
.key-task-form {
  padding: 20px;
}

.form-actions {
  text-align: right;
  margin-top: 20px;
}
</style>