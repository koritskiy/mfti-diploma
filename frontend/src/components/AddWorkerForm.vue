<template>
    <form @submit.prevent method="post">
        <h2 class="mt-6 text-3xl font-bold text-primary-content mb-5"> Добавление сотрудника в организацию </h2>
        <text-input
                label="Почта сотрудника"
                input-type="email"
                placeholder="Например, abc@gmail.com"
                class="mt-3"
                v-model:model-value="worker.email"
        />

        <text-input
                label="Имя сотрудника"
                placeholder="Например, Александр"
                class="mt-3"
                v-model:model-value="worker.firstName"
        />

        <text-input
                label="Фамилия сотрудника"
                placeholder="Например, Иванов"
                class="mt-3"
                v-model:model-value="worker.lastName"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createWorker" class="">Добавить сотрудника</success-outline-button>
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
            worker: {
                email: '',
                firstName: '',
                lastName: ''
            }
        }
    },
    methods: {
        createWorker() {
            this.$emit('create', this.worker)
        }
    }
}
</script>