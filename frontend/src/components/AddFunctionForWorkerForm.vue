<template>
    <form @submit.prevent method="post">
        <h2 class="mt-6 text-xl font-bold text-primary-content"> Привязать новую функцию к сотруднику </h2>

        <div class="form-control">
            <label class="label">
                <span class="label-text">Выберите должность</span>
            </label>
            <select class="select select-bordered w-full max-w-xs" v-model="linkFuncToWorker.selectFuncId">
                <option v-for="func in organization.functions" :key="func.id" :value="func.id">
                    {{ func.name }}
                </option>
            </select>
        </div>

        <text-input
                label="Тариф сотрудника"
                input-type="number"
                placeholder="Введите тариф"
                v-model:model-value="linkFuncToWorker.tariffRate"
        />

        <div class="flex justify-center">
            <success-outline-button @click="linkFunction" class="">Привязать</success-outline-button>
        </div>
    </form>
</template>

<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextInput from "@/components/ui/TextInput.vue";

export default {
    components: {SuccessOutlineButton, TextInput},
    props: {
        organization: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            linkFuncToWorker: {
                selectFuncId: "",
                tariffRate: ""
            }
        }
    },
    methods: {
        linkFunction() {
            this.$emit('create', this.linkFuncToWorker)
        }
    }
}
</script>