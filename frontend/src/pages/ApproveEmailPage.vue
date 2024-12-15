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
                    class="tab tab-active">
                Подтверждение почты
            </a>
            <a
                    @click="this.$router.push('/account/settings/upload/token')"
                    class="tab">
                Загрузка токена
            </a>
        </div>
    </div>

    <div class="flex flex-col items-center">
        <success-alert
                class="w-1/2"
                :is-success="isSuccess"
        >Вы успешно подтвердили почту!
        </success-alert>
        <error-alert
                class="w-1/2"
                :is-error="isError"
        > {{ errorDescription }}
        </error-alert>
    </div>

    <div class="flex justify-center mt-10">
        <div class="card bg-base-200 shadow-xl basis-1/2" v-show="this.isEmailApproved === false">
            <div class="card-body">
                <h2 class="card-title">Подтверждение почты</h2>
                    <approve-email-form @create="approveEmail" />
            </div>
        </div>

        <div class="alert alert-success shadow-lg basis-1/2" v-show="this.isEmailApproved === true">
            <div>
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                <span>Ваша почта успешно подтверждена!</span>
            </div>
        </div>
    </div>
</template>

<script>
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import ApproveEmailForm from "@/components/ApproveEmailForm.vue";
import axios from "axios";
import store from "@/store";
import Constants from "@/components/constants";

export default {
    components: {ApproveEmailForm, SuccessAlert, ErrorAlert},
    data() {
        return {
            isSuccess: false,
            isError: false,
            errorDescription: '',

            isEmailApproved: false,
            user: ''
        }
    },
    methods: {
        async approveEmail(secretCode) {
            await axios.put("http://localhost:8080/api/auth/v1/user/approve/email/" + secretCode, null, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isEmailApproved = response.data.payload.isEmailApproved;
                this.isSuccess = true;
            }).catch((response) => {
                console.log(response);
                this.isError = true;
                this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        async getUser() {
            await axios.get("http://localhost:8080/api/auth/v1/user/get/" + store.state.user.id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isEmailApproved = true;
            }).catch((response) => {
                console.log(response);
            })
        }
    },
    mounted() {
        this.$store.commit('setPageName', 'approve-email');
        this.getUser();
    }
}
</script>