_CLC_OVERLOAD _CLC_DECL event_t async_work_group_strided_copy( __local __CLC_GENTYPE *dst,const __global __CLC_GENTYPE *src, size_t num_gentypes, size_t src_stride, event_t e);
_CLC_OVERLOAD _CLC_DECL event_t async_work_group_strided_copy( __global __CLC_GENTYPE *dst,const __local __CLC_GENTYPE *src, size_t num_gentypes, size_t dst_stride, event_t e);
