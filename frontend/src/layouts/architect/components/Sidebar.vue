<template>
    <div :class="sidebarbg" class="app-sidebar sidebar-shadow" @mouseover="toggleSidebarHover('add','closed-sidebar-open')" @mouseleave="toggleSidebarHover('remove','closed-sidebar-open')">
        <div class="app-header__logo">
            <div class="logo-src" />
            <div class="header__pane ml-auto">
                <button type="button" class="hamburger close-sidebar-btn hamburger--elastic" v-bind:class="{ 'is-active' : isOpen }" @click="toggleBodyClass('closed-sidebar')">
                    <span class="hamburger-box">
                        <span class="hamburger-inner"></span>
                    </span>
                </button>
            </div>
        </div>
        <div class="app-sidebar-content">
            <VuePerfectScrollbar class="app-sidebar-scroll" v-once>
                <sidebar-menu :menu="menu" />
            </VuePerfectScrollbar>
        </div>
    </div>
</template>

<script>
import * as R from 'ramda';
import { SidebarMenu } from 'vue-sidebar-menu';
import VuePerfectScrollbar from 'vue-perfect-scrollbar';
import menu from '@/menu';

export default {
    components: {
        SidebarMenu,
        VuePerfectScrollbar,
    },
    props: {
        sidebarbg: {
            type: String,
            default: () => '',
        },
    },
    data() {
        return {
            isOpen: false,
            sidebarActive: false,
            collapsed: true,
            windowWidth: 0,
        };
    },
    computed: {
        menu() {
            const m = R.clone(menu);
            return m;
        },
    },
    mounted() {
        this.$nextTick(() => {
            window.addEventListener('resize', this.getWindowWidth);
            // Init
            this.getWindowWidth();
        });
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.getWindowWidth);
    },
    methods: {
        toggleBodyClass(className) {
            const el = document.body;
            this.isOpen = !this.isOpen;

            if (this.isOpen) {
                el.classList.add(className);
            } else {
                el.classList.remove(className);
            }
        },
        toggleSidebarHover(add, className) {
            const el = document.body;
            this.sidebarActive = !this.sidebarActive;

            this.windowWidth = document.documentElement.clientWidth;

            if (this.windowWidth > '992') {
                if (add === 'add') {
                    el.classList.add(className);
                } else {
                    el.classList.remove(className);
                }
            }
        },
        getWindowWidth() {
            const el = document.body;
            this.windowWidth = document.documentElement.clientWidth;

            if (this.windowWidth < '1350') {
                el.classList.add('closed-sidebar', 'closed-sidebar-md');
            } else {
                el.classList.remove('closed-sidebar', 'closed-sidebar-md');
            }
        },
    },
};
</script>

<style lang="scss">
.vsm-title {
    font-size: 1rem;
    font-weight: 400;
}
.app-sidebar.text-lighter .v-sidebar-menu .vsm-link {
    color: rgba(255, 255, 255, 0.85);
}
.active-item .vsm-title {
    font-weight: 700 !important;
}
.v-sidebar-menu {
    padding: 0.5rem !important;
}
.closed-sidebar .app-sidebar .v-sidebar-menu {
    padding: 0.5rem !important;
}
.closed-sidebar .v-sidebar-menu .vsm-link {
    padding: 0 !important;
}
.closed-sidebar-open.closed-sidebar .app-sidebar .v-sidebar-menu {
    padding: 0.5rem !important;

    .vsm-link {
        padding: 0 1.5rem 0 45px !important;
    }
}
.closed-sidebar-open.closed-sidebar .app-sidebar .logo-src {
    width: 175px !important;
}
.closed-sidebar .app-sidebar .logo-src {
    width: 60px !important;
}
.app-sidebar .app-header__logo {
    padding: 0 1rem !important;
}
</style>
