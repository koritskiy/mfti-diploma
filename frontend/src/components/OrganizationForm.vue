<template>
    <form @submit.prevent method="post">
        <h2 class="mt-6 text-3xl font-bold text-primary-content"> Форма создания организации </h2>
        <text-input
                label="Название организации"
                placeholder="Например, Кодемика"
                class="mt-3"
                v-model:model-value="organization.name"
        />

        <text-input
                label="Адрес организации"
                placeholder="Например, Удальцова, дом 30"
                class="mt-3"
                v-model:model-value="organization.address"
        />

        <text-input
                label="Ссылка на сайт организации"
                placeholder="Например, ya.ru"
                class="mt-3"
                v-model:model-value="organization.link"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createOrganization" class="">Создать организацию</success-outline-button>
        </div>
    </form>
</template>

<script>
import TextInput from "@/components/ui/TextInput.vue";
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";

export default {
    components: {SuccessOutlineButton, TextInput},
    data() {
        return {
            organization: {
                name: '',
                address: '',
                link: ''
            }
        }
    },
    methods: {
        createOrganization() {
            this.$emit('create', this.organization)
        }
    }
}
</script>

<style scoped>

</style>