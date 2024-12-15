<template>
    <div class="form-control w-full">
        <label class="label">
            <span class="label-text">{{ label }}</span>
        </label>
        <input
                :value="modelValue"
                @input="updateInput"
                :type="inputType"
                class="input input-bordered input-md w-full"
                :placeholder="placeholder"
        />
    </div>
</template>

<script>
export default {
    name: 'text-input',
    props: {
        label: {
            type: String,
            required: true
        },
        inputType: {
            type: String,
            default: "text",
            required: false
        },
        placeholder: {
            type: String,
            required: false
        },
        modelValue: [String]
    },
    methods: {
        updateInput(event) {
            this.$emit('update:modelValue', event.target.value)
        }
    }
}
</script>

<style lang="scss" scoped>

</style>