<template>
    <div class="app-container app-theme-white">
        <transition name="fade" mode="out-in" appear>
            <Header :headerbg="'null'"/>
        </transition>
        <transition name="fade" mode="out-in" appear>
            <Sidebar :sidebarbg="'bg-white'"/>
        </transition>
        <div class="app-main__outer">
            <div class="app-main__inner">
                <slot></slot>
            </div>
            <transition name="fade" mode="out-in" appear>
                <Footer/>
            </transition>
        </div>
    </div>
</template>

<script>
import Header from './components/Header.vue';
import Footer from './components/Footer.vue';
import Sidebar from './components/Sidebar.vue';


export default {
    name: 'BaseLayout',
    components: {
        Header,
        Footer,
        Sidebar,
    },
};
</script>
