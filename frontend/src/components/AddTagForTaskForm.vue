<template>
    <form @submit.prevent method="post">
        <div class="form-control">
            <label class="label">
                <span class="label-text">Выберите тег</span>
            </label>
            <select class="select select-bordered w-full" v-model="tag.selectId">
                <option v-for="tag in organization.tags" :key="tag.id" :value="tag.id">
                    {{ tag.name }}
                </option>
            </select>
        </div>

        <div class="flex justify-center">
            <success-outline-button @click="linkTag" class="">Привязать</success-outline-button>
        </div>
    </form>
</template>
<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";

export default {
    components: {SuccessOutlineButton},
    props: {
        task: {
            type: Object,
            required: true
        },
        organization: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            tag: {
                selectId: 0
            }
        }
    },
    methods: {
        linkTag() {
            this.$emit('create', this.tag)
        }
    }
}
</script>