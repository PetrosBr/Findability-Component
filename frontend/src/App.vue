<template>
    <component :is="layout">
        <router-view :layout.sync="layout" />
    </component>
</template>

<script>
import { BaseLayout, PagesLayout } from '@/layouts';

export default {
    name: 'App',
    components: { BaseLayout, PagesLayout },
    computed: {
        layout() {
            switch (this.$route.name) {
            case 'error404':
            case 'error500':
            case null:
                return PagesLayout;
            default:
                return BaseLayout;
            }
        },
    },
};
</script>


<style lang="scss">
@import "@/assets/scss/style.scss";
</style>
