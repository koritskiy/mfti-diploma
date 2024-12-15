<template>
    <div class="flex flex-col items-center">
        <success-alert
            class="w-1/2"
            :is-success="isSuccess"
        >Вы успешно создали организацию!
        </success-alert>
        <error-alert
            class="w-1/2"
            :is-error="isError"
        > {{ errorDescription }}
        </error-alert>
    </div>
    <div class="flex justify-center">
        <organization-form @create="createOrganization"/>
    </div>
</template>

<script>
import OrganizationForm from "@/components/OrganizationForm.vue";
import axios from "axios";
import store from "@/store";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {SuccessAlert, ErrorAlert, OrganizationForm},
    data() {
        return {
            isSuccess: false,
            isError: false,
            errorDescription: ''
        }
    },
    methods: {
        async createOrganization(organization) {
            if (organization.name === '' || organization.address === '' || organization.link === '') {
                this.isError = true;
                this.errorDescription = Constants.ERROR_EMPTY_FIELDS;
                return
            }
            await axios.post('http://localhost:8080/api/tracker/v1/organization/create', {
                name: organization.name,
                address: organization.address,
                link: organization.link,
                ownerUserId: store.state.user.id,
            }, {
                headers: {
                    "Authorization": store.state.token
                }
            }).then((response) => {
                this.isSuccess = true;
                const organizationId = response.data.payload.id;
                setTimeout(() => this.$router.push('/organizations/' + organizationId), 1000)
            }).catch((response) => {
                this.isError = true;
                this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION;
                console.log(response)
            });
        }
    },
    mounted() {
        this.$store.commit('setPageName', 'create-organization-page');
    }
}
</script>

<style lang="scss" scoped>

</style>