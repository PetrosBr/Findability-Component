<template>
    <div class="h-100 bg-animation bg-mobispaces-gray-2">
        <div class="d-flex h-75 justify-content-center align-items-center">
            <b-col md="6" class="mx-auto app-login-box">
                <div class="modal-dialog w-100">
                    <div class="modal-content">
                        <div class="modal-body">
                            <div class="row justify-content-center">
                                <div class="app-logo mx-auto mb-1"/>

                                <div class="col-md-12 text-center">
                                    <span class="display-1 d-block text-muted">404</span>
                                    <div class="mb-4 lead text-dark">We couldn't find the page you are looking for :(</div>
                                    <b-button class="mobispaces-button-default" variant="mobispaces-blue" @click="toHome">Back to Home</b-button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </b-col>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Error404',
    methods: {
        toHome() {
            this.$router.push({ name: 'Home' });
        },
    },
};
</script>
