<template>
  <div class="dashboard-container">
    <div class="dashboard-layout">
      <!-- 中间主内容区 -->
      <div class="main-content">
        <!-- 顶部：快捷入口 & 公告通知 -->
        <div class="section-row top-row">
          <div class="section-card quick-access">
            <div class="section-header">
              <span class="title">快捷入口</span>
              <i class="el-icon-more"></i>
            </div>
            <div class="quick-grid">
              <div v-for="item in quickLinks" :key="item.name" class="quick-item">
                <div class="quick-icon" :style="{ backgroundColor: item.bg }">
                  <i :class="item.icon" :style="{ color: item.color }"></i>
                </div>
                <span class="quick-label">{{ item.name }}</span>
              </div>
            </div>
          </div>
          <div class="section-card notice-board">
            <div class="section-header">
              <span class="title">公告通知</span>
              <el-link type="primary" :underline="false">更多</el-link>
            </div>
            <div class="notice-list">
              <div v-for="i in 4" :key="i" class="notice-item">
                <span class="notice-tag">[通知]</span>
                <span class="notice-content">关于系统安全加固的通知公告...</span>
                <span class="notice-date">02-26</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 下方主体区域 -->
        <div class="bottom-main-area">
          <div class="left-column">
            <!-- 我的关注 -->
            <div class="section-card my-follows">
              <div class="section-header">
                <span class="title">我的关注</span>
                <el-link type="primary" :underline="false">更多</el-link>
              </div>
              <div class="follow-list">
                <div v-for="i in 3" :key="i" class="follow-item">
                  <div class="follow-info">
                    <div class="follow-title">项目进度汇报 - 璇玑系统二期</div>
                    <div class="follow-time">2026-02-26 10:00</div>
                  </div>
                  <el-tag size="mini" type="success">进行中</el-tag>
                </div>
              </div>
            </div>

            <!-- 科技团队 -->
            <div class="section-card tech-team">
              <div class="section-header">
                <span class="title">科技团队</span>
              </div>
              <div class="team-content">
                <div class="team-member-type">
                  <div class="type-label">项目经理</div>
                  <div class="member-cards">
                    <div class="member-card" v-for="i in 2" :key="'pm'+i">
                      <el-avatar :size="32" icon="el-icon-user"></el-avatar>
                      <div class="member-info">
                        <div class="member-name">张经理</div>
                        <div class="member-role">高级经理</div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="team-member-type">
                  <div class="type-label">数据分析师</div>
                  <div class="member-cards">
                    <div class="member-card" v-for="i in 2" :key="'da'+i">
                      <el-avatar :size="32" icon="el-icon-user"></el-avatar>
                      <div class="member-info">
                        <div class="member-name">李分析</div>
                        <div class="member-role">资深分析</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 右栏：工作日历 & 今日任务 -->
          <div class="right-column">
            <div class="section-card calendar-card">
              <div class="section-header">
                <span class="title">工作日历</span>
              </div>
              <el-calendar v-model="calendarValue" class="mini-calendar">
                <template slot="dateCell" slot-scope="{date, data}">
                  <div class="calendar-day" @click="handleDateClick(data.day)">
                    <span class="day-num">{{ data.day.split('-').slice(2).join('') }}</span>
                    <div class="task-indicators">
                      <div v-for="(task, index) in getTasksForDate(data.day)" :key="index" class="task-dot"></div>
                    </div>
                  </div>
                </template>
              </el-calendar>
            </div>

            <div class="section-card today-tasks">
              <div class="section-header">
                <span class="title">今日任务</span>
                <span class="date-label">{{ formattedSelectedDate }}</span>
              </div>
              <div class="task-list">
                <div v-if="getTasksForDate(selectedDateStr).length === 0" class="empty-tasks">
                  <i class="el-icon-document-remove"></i>
                  <span>暂无任务，点击日历新增</span>
                </div>
                <div v-for="(task, index) in getTasksForDate(selectedDateStr)" :key="index" class="task-item">
                  <div class="task-dot-prefix"></div>
                  <div class="task-text">{{ task.content }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 新增任务对话框 -->
      <el-dialog title="新增工作任务" :visible.sync="taskDialogVisible" width="400px" append-to-body>
        <el-form :model="taskForm" label-width="80px">
          <el-form-item label="任务日期">
            <el-input v-model="taskForm.date" disabled />
          </el-form-item>
          <el-form-item label="任务内容">
            <el-input v-model="taskForm.content" type="textarea" placeholder="请输入任务内容" :rows="3" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="taskDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveTask">确 定</el-button>
        </div>
      </el-dialog>

      <!-- 右侧：用户信息 & 部门员工 -->
      <div class="side-content">
        <div class="user-profile-card">
          <div class="user-bg"></div>
          <div class="user-main-info">
            <el-avatar :size="64" class="user-avatar" icon="el-icon-user"></el-avatar>
            <h2 class="user-name">{{ name }}</h2>
            <div class="user-info-grid">
              <div class="grid-item">
                <span class="label">当前角色</span>
                <span class="value">{{ roles[0] || '管理员' }}</span>
              </div>
              <div class="grid-item">
                <span class="label">个人身份</span>
                <span class="value">正式员工</span>
              </div>
              <div class="grid-item">
                <span class="label">办公地点</span>
                <span class="value">北京分行·12F</span>
              </div>
              <div class="grid-item">
                <span class="label">办公电话</span>
                <span class="value">010-888888</span>
              </div>
              <div class="grid-item">
                <span class="label">入职时间</span>
                <span class="value">2024-01-01</span>
              </div>
              <div class="grid-item">
                <span class="label">员工状态</span>
                <span class="value">在线</span>
              </div>
            </div>
          </div>
          <div class="user-stats">
            <div class="stat-item">
              <div class="stat-num">12</div>
              <div class="stat-label">待办任务</div>
            </div>
            <div class="stat-item">
              <div class="stat-num">8</div>
              <div class="stat-label">进行中</div>
            </div>
            <div class="stat-item">
              <div class="stat-num">45</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </div>

        <div class="department-card">
          <div class="section-header">
            <span class="title">我的部门</span>
            <span class="count">12人</span>
          </div>
          <div class="department-grid">
            <div v-for="i in 8" :key="i" class="dept-user-item">
              <el-tooltip :content="'同事 ' + i" placement="top">
                <el-avatar :size="36" icon="el-icon-user"></el-avatar>
              </el-tooltip>
              <span class="dept-user-name">同事 {{ i }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  data() {
    return {
      calendarValue: new Date(),
      taskDialogVisible: false,
      taskForm: {
        date: '',
        content: ''
      },
      tasks: {}, // 格式: { '2026-02-26': [{ content: '任务1' }] }
      quickLinks: [
        { name: '用户管理', icon: 'el-icon-user', color: '#1890ff', bg: '#e6f7ff' },
        { name: '机构管理', icon: 'el-icon-office-building', color: '#52c41a', bg: '#f6ffed' },
        { name: '角色授权', icon: 'el-icon-key', color: '#faad14', bg: '#fff7e6' },
        { name: '菜单配置', icon: 'el-icon-setting', color: '#f5222d', bg: '#fff1f0' },
        { name: '系统日志', icon: 'el-icon-document', color: '#13c2c2', bg: '#e6fffb' },
        { name: '个人设置', icon: 'el-icon-user-solid', color: '#722ed1', bg: '#f9f0ff' }
      ]
    }
  },
  computed: {
    ...mapGetters(['name', 'roles']),
    selectedDateStr() {
      if (!this.calendarValue) return '';
      const d = new Date(this.calendarValue);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    formattedSelectedDate() {
      if (!this.calendarValue) return '';
      const d = new Date(this.calendarValue);
      return `${d.getMonth() + 1}月${d.getDate()}日`;
    }
  },
  methods: {
    handleDateClick(day) {
      this.taskForm.date = day;
      this.taskForm.content = '';
      this.taskDialogVisible = true;
    },
    saveTask() {
      if (!this.taskForm.content) {
        this.$message.warning('请输入任务内容');
        return;
      }
      if (!this.tasks[this.taskForm.date]) {
        this.$set(this.tasks, this.taskForm.date, []);
      }
      this.tasks[this.taskForm.date].push({ content: this.taskForm.content });
      this.taskDialogVisible = false;
      this.$message.success('任务添加成功');
    },
    getTasksForDate(day) {
      return this.tasks[day] || [];
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  height: calc(100vh - 50px); // 减去Navbar高度
  background-color: #f0f2f5;
  padding: 16px;
  overflow: hidden; // 禁止滚动

  .dashboard-layout {
    display: flex;
    height: 100%;
    gap: 16px;
  }

  .main-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 16px;
    min-width: 0; // 防止Flex溢出
  }

  .side-content {
    width: 320px;
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .section-row {
    display: flex;
    gap: 16px;
  }

  .top-row { height: 25%; }

  .bottom-main-area {
    flex: 1;
    display: flex;
    gap: 16px;
    min-height: 0;
  }

  .left-column {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 16px;
    min-width: 0;
    .my-follows { flex: 1; }
    .tech-team { flex: 1; }
  }

  .right-column {
    flex: 1.5;
    display: flex;
    flex-direction: column;
    gap: 16px;
    min-width: 0;
    .calendar-card { flex: 1.8; } // 压缩日历
    .today-tasks { flex: 1.2; } // 今日任务列表
  }

  .section-card {
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    box-shadow: 0 1px 4px rgba(0,21,41,0.08);

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
      
      .title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        position: relative;
        padding-left: 12px;
        
        &::before {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 4px;
          height: 16px;
          background: #1890ff;
          border-radius: 2px;
        }
      }
    }
  }

  // 快捷入口
  .quick-access {
    flex: 2;
    .quick-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 12px;
      flex: 1;
      align-items: center;
    }
    .quick-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      cursor: pointer;
      transition: all 0.3s;
      &:hover { transform: translateY(-2px); }
      .quick-icon {
        width: 40px;
        height: 40px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        margin-bottom: 4px;
      }
      .quick-label { font-size: 12px; color: #606266; }
    }
  }

  // 我的关注
  .my-follows {
    flex: 3;
    .follow-list {
      display: flex;
      flex-direction: column;
      gap: 8px;
      overflow: hidden;
    }
    .follow-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px;
      background: #fafafa;
      border-radius: 4px;
      .follow-title { font-size: 13px; color: #303133; margin-bottom: 4px; }
      .follow-time { font-size: 12px; color: #909399; }
    }
  }

  // 公告通知
  .notice-board {
    flex: 1;
    .notice-list {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }
    .notice-item {
      display: flex;
      align-items: center;
      font-size: 13px;
      .notice-tag { color: #f5222d; margin-right: 8px; }
      .notice-content { flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
      .notice-date { color: #909399; margin-left: 8px; }
    }
  }

  // 工作日历
  .calendar-card {
    ::v-deep .el-calendar {
      height: 100%;
      .el-calendar__header { display: none; }
      .el-calendar__body { padding: 0; height: 100%; }
      .el-calendar-table {
        height: 100%;
        table-layout: fixed;
        thead th { padding: 8px 0; font-size: 13px; color: #909399; }
        .el-calendar-day {
          height: 100%;
          min-height: 36px;
          padding: 2px;
          transition: background 0.3s;
          &:hover { background-color: #f2f6fc; }
        }
        td.is-selected { background-color: #e6f7ff; }
      }
    }
    .calendar-day {
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      .day-num { font-size: 14px; font-weight: 500; }
      .task-indicators {
        margin-top: 4px;
        display: flex;
        flex-wrap: wrap;
        gap: 2px;
        justify-content: center;
        .task-dot {
          width: 6px;
          height: 6px;
          background: #1890ff;
          border-radius: 50%;
        }
      }
    }
  }

  // 今日任务
  .today-tasks {
    .date-label { font-size: 12px; color: #909399; font-weight: normal; }
    .task-list {
      flex: 1;
      overflow-y: auto;
      padding-right: 4px;
      &::-webkit-scrollbar { width: 4px; }
      &::-webkit-scrollbar-thumb { background: #e8e8e8; border-radius: 2px; }
    }
    .task-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 8px 0;
      border-bottom: 1px dotted #f0f0f0;
      &:last-child { border-bottom: none; }
      .task-dot-prefix {
        width: 6px;
        height: 6px;
        background: #1890ff;
        border-radius: 50%;
        flex-shrink: 0;
      }
      .task-text { font-size: 13px; color: #606266; }
    }
    .empty-tasks {
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #c0c4cc;
      gap: 8px;
      font-size: 13px;
      i { font-size: 24px; }
    }
  }

  // 科技团队
  .tech-team {
    .team-content {
      display: flex;
      gap: 32px;
      flex: 1;
    }
    .team-member-type {
      flex: 1;
      .type-label { font-size: 14px; font-weight: 600; color: #606266; margin-bottom: 12px; }
      .member-cards { display: flex; gap: 16px; }
      .member-card {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px;
        background: #f5f7fa;
        border-radius: 8px;
        flex: 1;
        .member-name { font-size: 14px; font-weight: 600; color: #303133; }
        .member-role { font-size: 12px; color: #909399; }
      }
    }
  }

  // 右侧用户信息
  .user-profile-card {
    background: #fff;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 1px 4px rgba(0,21,41,0.08);
    .user-bg {
      height: 100px;
      background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
    }
    .user-main-info {
      text-align: center;
      margin-top: -32px;
      padding: 0 16px 16px;
      border-bottom: 1px solid #f0f0f0;
      .user-avatar { border: 4px solid #fff; box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1); }
      .user-name { margin: 8px 0 16px; font-size: 18px; color: #303133; }
      
      .user-info-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 12px;
        text-align: left;
        
        .grid-item {
          display: flex;
          flex-direction: column;
          gap: 2px;
          .label { font-size: 12px; color: #909399; }
          .value { font-size: 13px; color: #606266; font-weight: 500; }
        }
      }
    }
    .user-stats {
      display: flex;
      padding: 16px 0;
      .stat-item {
        flex: 1;
        text-align: center;
        border-right: 1px solid #f0f0f0;
        &:last-child { border-right: none; }
        .stat-num { font-size: 18px; font-weight: 600; color: #1890ff; }
        .stat-label { font-size: 12px; color: #909399; margin-top: 4px; }
      }
    }
  }

  // 部门员工
  .department-card {
    flex: 1;
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    box-shadow: 0 1px 4px rgba(0,21,41,0.08);
    .count { font-size: 12px; color: #909399; font-weight: normal; }
    .department-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
      padding-top: 8px;
      flex: 1;
      overflow-y: auto;
      &::-webkit-scrollbar { width: 4px; }
      &::-webkit-scrollbar-thumb { background: #e8e8e8; border-radius: 2px; }
      
      .dept-user-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 4px;
        .dept-user-name { font-size: 11px; color: #909399; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 100%; }
      }
    }
  }
}
</style>
