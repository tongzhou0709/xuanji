<template>
  <div class="key-task-detail">
    <div v-loading="loading" class="detail-container">
      <div class="detail-left">
        <div class="detail-section">
          <h3 class="section-title">主要信息</h3>
          <div class="info-item">
            <label>主要领域：</label>
            <span>{{ taskDetail.mainDomain }}</span>
          </div>
          <div class="info-item">
            <label>重点任务名称：</label>
            <span>{{ taskDetail.taskName }}</span>
          </div>
          <div class="info-item">
            <label>具体举措：</label>
            <div class="content-text">{{ taskDetail.specificMeasures }}</div>
          </div>
          <div class="info-item">
            <label>预期实现效果：</label>
            <div class="content-text">{{ taskDetail.expectedEffect }}</div>
          </div>
          <div class="info-item">
            <label>标志成果或交付物：</label>
            <div class="content-text">{{ taskDetail.deliverables }}</div>
          </div>
        </div>
      </div>
      
      <div class="detail-right">
        <div class="detail-section">
          <h3 class="section-title">辅助信息</h3>
          <div class="info-item">
            <label>关键词：</label>
            <div class="keywords">
              <el-tag v-if="taskDetail.keyword1" size="small" type="primary">{{ taskDetail.keyword1 }}</el-tag>
              <el-tag v-if="taskDetail.keyword2" size="small" type="success" style="margin-left: 5px;">{{ taskDetail.keyword2 }}</el-tag>
              <el-tag v-if="taskDetail.keyword3" size="small" type="warning" style="margin-left: 5px;">{{ taskDetail.keyword3 }}</el-tag>
            </div>
          </div>
          <div class="info-item">
            <label>牵头部门：</label>
            <span>{{ taskDetail.leadDepartment }}</span>
          </div>
          <div class="info-item">
            <label>牵头人：</label>
            <span>{{ taskDetail.leadPerson }}</span>
          </div>
          <div class="info-item">
            <label>计划开始时间：</label>
            <span>{{ taskDetail.plannedStartDate }}</span>
          </div>
          <div class="info-item">
            <label>计划完成时间：</label>
            <span>{{ taskDetail.plannedEndDate }}</span>
          </div>
          <div class="info-item">
            <label>创建时间：</label>
            <span>{{ taskDetail.createTime }}</span>
          </div>
          <div class="info-item">
            <label>创建人：</label>
            <span>{{ taskDetail.createBy }}</span>
          </div>
          <div class="info-item">
            <label>更新时间：</label>
            <span>{{ taskDetail.updateTime }}</span>
          </div>
          <div class="info-item">
            <label>更新人：</label>
            <span>{{ taskDetail.updateBy }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getKeyTaskById } from '@/api/keyTask'

export default {
  name: 'KeyTaskDetail',
  props: {
    taskId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      taskDetail: {}
    }
  },
  watch: {
    taskId: {
      handler(newVal) {
        if (newVal) {
          this.getTaskDetail()
        }
      },
      immediate: true
    }
  },
  methods: {
    getTaskDetail() {
      this.loading = true
      getKeyTaskById(this.taskId).then(response => {
        this.taskDetail = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.key-task-detail {
  padding: 20px;
}

.detail-container {
  display: flex;
  gap: 30px;
}

.detail-left {
  flex: 2;
}

.detail-right {
  flex: 1;
}

.detail-section {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-title {
  margin-top: 0;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eaeaea;
  color: #333;
  font-size: 18px;
  font-weight: bold;
}

.info-item {
  margin-bottom: 15px;
}

.info-item label {
  display: inline-block;
  width: 120px;
  font-weight: bold;
  color: #606266;
  vertical-align: top;
}

.content-text {
  display: inline-block;
  width: calc(100% - 130px);
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.6;
  vertical-align: top;
}

.keywords {
  display: inline-block;
  vertical-align: top;
}
</style>