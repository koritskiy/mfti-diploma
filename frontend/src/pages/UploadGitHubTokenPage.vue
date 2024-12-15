<template>
    <div class="flex justify-center mt-10">
        <div class="tabs tabs-boxed">
            <a
                    @click="this.$router.push('/account/settings')"
                    class="tab">
                Редактирование данных
            </a>
            <a
                    @click="this.$router.push('/account/settings/approve/email')"
                    class="tab">
                Подтверждение почты
            </a>
            <a
                    @click="this.$router.push('/account/settings/upload/token')"
                    class="tab tab-active">
                Загрузка токена
            </a>
        </div>
    </div>

    <div class="flex flex-col items-center">
        <success-alert
                class="w-1/2"
                :is-success="isSuccess"
        >Вы успешно загрузили токен!
        </success-alert>
        <error-alert
                class="w-1/2"
                :is-error="isError"
        > {{ errorDescription }}
        </error-alert>
    </div>

    <div class="flex justify-center mt-10">
        <div class="card bg-base-100 shadow-xl basis-1/2">
            <div class="card-body">
                <h2 class="card-title">Загрузка токена</h2>
                <upload-token-form @create="uploadToken"/>
            </div>
        </div>
    </div>
</template>

<script>
import UploadTokenForm from "@/components/UploadTokenForm.vue";
import axios from "axios";
import store from "@/store";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {ErrorAlert, SuccessAlert, UploadTokenForm},
    data() {
        return {
            isSuccess: false,
            isError: false,
            errorDescription: ''
        }
    },
    methods: {
        async uploadToken(token) {
            await axios.put('http://localhost:8080/api/auth/v1/user/upload/token/github', {
                token: token
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.isSuccess = true;
                this.$store.commit('setGitHubToken', response.data.payload.gitHubToken)
                setTimeout(() => this.$router.push('/account'), 1000)
                console.log(response.data.payload);
            }).catch((response) => {
                this.isError = true;
                this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION;
                console.log(response)
            })
        }
    },
    mounted() {
        this.$store.commit('setPageName', 'upload-git-hub-token-page');
    }
}
</script>