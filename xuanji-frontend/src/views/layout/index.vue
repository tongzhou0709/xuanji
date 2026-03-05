<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <div :class="{'fixed-header':fixedHeader}">
        <navbar />
      </div>
      <app-main />
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'

export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  data() {
    return {
      fixedHeader: true
    }
  },
  created() {
    this.$store.dispatch('user/getInfo')
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/variables.scss";

.app-wrapper {
  position: relative;
  height: 100vh;
  width: 100%;
  overflow: hidden;
  display: flex;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.sidebar-container {
  height: 100%;
  width: $sideBarWidth !important;
  flex-shrink: 0;
  overflow: hidden;
  transition: width 0.28s;
  
  & > div {
    width: $sideBarWidth !important;
  }
}

.hideSidebar .sidebar-container {
  width: 0 !important;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;
}

.fixed-header {
  flex-shrink: 0;
  z-index: 9;
  width: 100%;
  background: #fff;
}
</style>
