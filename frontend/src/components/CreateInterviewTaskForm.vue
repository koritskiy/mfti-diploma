<template>
    <form @submit.prevent method="post">
        <text-input
                label="Название задачи"
                placeholder="Например, сортировка пузырьком"
                v-model:model-value="task.name"
        />

        <textarea-element
            placeholder="Введите формулировку задачи"
            label="Формулировка задачи"
            v-model:model-value="task.question"
        />

        <textarea-element
            placeholder="Введите ответ на задачу"
            label="Ответ на задачу"
            v-model:model-value="task.answer"
        />

        <div class="form-control mt-3 mb-3">
            <label class="label">
                <span class="label-text">Выберите категорию</span>
            </label>
            <select class="select select-bordered w-full" v-model="task.taskCategoryId">
                <option disabled selected>Выберите категорию задачи</option>
                <option
                    v-for="c in categories" :key="c.id" :value="c.id"
                >
                    {{ c.name }}
                </option>
            </select>
        </div>

        <div class="flex justify-center">
            <success-outline-button @click="createTask" class="">Создать задачу</success-outline-button>
        </div>
    </form>
</template>

<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextInput from "@/components/ui/TextInput.vue";
import TextareaElement from "@/components/ui/TextareaElement.vue";

export default {
    components: {TextareaElement, TextInput, SuccessOutlineButton},
    props: {
        categories: {
            type: Array,
            required: true
        }
    },
    data() {
        return {
            task: {
                name: '',
                question: '',
                answer: '',
                taskCategoryId: 0
            }
        }
    },
    methods: {
        createTask() {
            this.$emit('create', this.task)
        }
    }
}
</script>