<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="false" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="false"
        :background-color="menuBg"
        :text-color="menuText"
        :unique-opened="false"
        :active-text-color="menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'

export default {
  components: { SidebarItem, Logo },
  data() {
    return {
      menuBg: '#FFFFFF',
      menuText: '#606266',
      menuActiveText: '#409EFF'
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'routes'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return true
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>
